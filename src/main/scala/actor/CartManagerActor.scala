package actor

import akka.actor.{Actor, Props}
import message.{AddItem, AddedItemEvent, CreateCart}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}


class CartManagerActor extends Actor {

  // additional two implicitly available objects needed for Futures
  implicit val executionContext = context.dispatcher
  implicit val timeout: Timeout = 5 seconds



  override def receive = {

    case CreateCart(id) =>
      context.actorOf(Props(classOf[CartActor], id), "cart-"+id)

    case msg @ AddItem(id, item) =>
      val childRef = context.child("cart-"+id)
      childRef match {
//        case Some(cardRef) => cardRef ! msg // ! = tell function
        case Some(cardRef) =>
          val result: Future[AddedItemEvent] = (cardRef ? msg).mapTo[AddedItemEvent] // ? = ask function, returns a Future[Any] - if not mapped to a specified type
          result onComplete { // onComplete takes a function object of type: Try[T] => U
            case Success(event)   => println(s"Received event: $event")
            case Failure(failure) => println(s"Failed while waiting for event: ${failure.getMessage}")
          }
        case None => println(s"no such cart (yet) with id $id")
      }
//    // needed if not "asking" with ?
//    case event : AddedItemEvent =>
//      println(s"received an answer: $event")
  }

}
