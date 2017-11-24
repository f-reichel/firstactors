package actor

import akka.actor.{Actor, ActorRef, ActorSelection}
import message._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.util.{Failure, Success}


class CartActor(var id:String) extends Actor {

  var content = Map[String, Int]()

  val paymentActor        = context.actorSelection("/user/payment-actor")
  val deliveryActor       = context.actorSelection("/user/delivery-actor")
  val communicationsActor = context.actorSelection("/user/communications-actor")

  // additional two implicitly available objects needed for Futures
  implicit val executionContext = context.dispatcher
  implicit val timeout: Timeout = 5 seconds

  println(s"CartActor($id) created")

  override def receive = {
    case AddItem(id,item) =>
      println(s"adding $item to cart of $id")
      content = content + (item -> 1)
      println(s"cart of $id is no containing: $content")
      sender() ! AddedItemEvent(item)

    case CheckOut(forId) =>
      val checkOutResult =
        for {
          paymentEvent   <- (paymentActor ? CollectPayment(100.0, "DE12345", s"Thanks $forId")).mapTo[PaymentCollectedEvent]
          deliveredEvent <- (deliveryActor ? Deliver(content, s"$forId, Unistr. 31, Regensburg")).mapTo[DeliveredEvent]
          sentEvent      <- (communicationsActor ? SendConfirmation(s"user@mail.com", s"Thanks $forId for buying. Payment id = ${paymentEvent.transactionId}. Tracking id = ${deliveredEvent.trackingId}")).mapTo[SentConfirmationEvent]
        } yield CheckedOutEvent(paymentEvent.transactionId, deliveredEvent.trackingId)

      checkOutResult.onComplete{
        case Success(checkedOutEvent) =>
          println(s"Done all! Went well!")
          sender() ! checkedOutEvent
        case Failure(failureMessage) =>
          println(s"Don't know what to do?!?")
        failureMessage.printStackTrace()// TODO !!!!!!!!!
      }


  }
}
