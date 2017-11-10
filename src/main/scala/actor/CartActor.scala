package actor

import akka.actor.{Actor, ActorRef}
import message._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.util.{Failure, Success}


class CartActor(var id:String) extends Actor {

  var content = Map[String, Int]()

  var paymentActor: ActorRef = _ // TODO!!!!!
  var deliveryActor: ActorRef = _ // TODO!!!!!
  var communicationsActor: ActorRef = _ // TODO!!!!!!

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

    case CheckOut =>

      (paymentActor ? CollectPayment(100.0, "DE12345", "Thanks")).mapTo[PaymentCollectedEvent]
      .flatMap {
        paymentEvent => {
          (deliveryActor ? Deliver(content, "Unistr. 31, Regensburg")).mapTo[DeliveredEvent]
        }
      }.flatMap {
        deliveredEvent =>
          (communicationsActor ? SendConfirmation(s"user@mail.com", "Thanks for buying. Tracking id = ???")).mapTo[SentConfirmationEvent]
      }.onComplete{
        case Success(sentEvent) =>
          println(s"Done all! Went well!")
          sender() ! "Check out successful"  // TODO!!!!!!!
        case Failure(failureMessage) =>
      }
  }
}
