package actor

import akka.actor.{Actor, ActorLogging}
import message._
import akka.pattern.ask
import akka.persistence.PersistentActor
import akka.util.Timeout

import scala.concurrent.duration._
import scala.util.{Failure, Success}


class CartActor(var id:String) extends PersistentActor with ActorLogging {

  var content = Map[String, Int]()

  val paymentActor        = context.actorSelection("/user/payment-actor")
  val deliveryActor       = context.actorSelection("/user/delivery-actor")
  val communicationsActor = context.actorSelection("/user/communications-actor")

  // additional two implicitly available objects needed for Futures
  implicit val executionContext = context.dispatcher
  implicit val timeout: Timeout = 5 seconds

  log.info(s"CartActor($id) created")


  // helper function in order to add item to map
  def addItem(event:AddItemPersistenceEvent) = {
    log.info(s"adding ${event.item} to cart of ${event.id}")
    content = content + (event.item -> 1)
    log.info(s"cart of ${event.id} is no containing: $content")
    sender() ! AddedItemEvent(event.item)
  }


  override def persistenceId: String = id

  override def receiveRecover = {
    case event : AddItemPersistenceEvent =>
      addItem(event)
    case event : CheckOutPersistenceEvent =>
      content = Map()
  }

  override def receiveCommand = {
    case AddItem(id, item) =>
      persist(AddItemPersistenceEvent(id,item))(addItem)
    case CheckOut(forId) =>
      if(forId != id)
        throw new CartStateException(s"check out received for $forId but I am $id")
      persist(CheckOutPersistenceEvent) { (event) => {
          val checkOutResult =
            for {
              paymentEvent <- (paymentActor ? CollectPayment(100.0, "DE12345", s"Thanks $forId")).mapTo[PaymentCollectedEvent]
              deliveredEvent <- (deliveryActor ? Deliver(content, s"$forId, Unistr. 31, Regensburg")).mapTo[DeliveredEvent]
              sentEvent <- (communicationsActor ? SendConfirmation(s"user@mail.com", s"Thanks $forId for buying. Payment id = ${paymentEvent.transactionId}. Tracking id = ${deliveredEvent.trackingId}")).mapTo[SentConfirmationEvent]
            } yield CheckedOutEvent(paymentEvent.transactionId, deliveredEvent.trackingId)

          checkOutResult.onComplete {
            case Success(checkedOutEvent) =>
              log.info(s"Cart $id: checkout items $content")
              sender() ! checkedOutEvent
              content = Map()
            case Failure(failureMessage) =>
              log.info(s"Don't know what to do?!?")
              failureMessage.printStackTrace() // TODO !!!!!!!!!
          }
        }
      }


  }
}
