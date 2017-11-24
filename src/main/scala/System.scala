import actor._
import akka.actor.{ActorRef, ActorSystem, Props}
import message.{AddItem, CheckOut, CreateCart}

import scala.io.StdIn
import scala.concurrent.duration._

object System extends App {
  println("Starting up my first actor system")

  val system = ActorSystem("my-first-system")

  val firstActor: ActorRef = system.actorOf(Props[FirstActor], "first-actor")
  val cartManagerActor = system.actorOf(Props[CartManagerActor], "cart-manager")
  system.actorOf(Props[PaymentActor], "payment-actor")
  system.actorOf(Props[DeliveryActor], "delivery-actor")
  system.actorOf(Props[CommunicationsActor], "communications-actor")


  cartManagerActor ! CreateCart("tom")
  cartManagerActor ! CreateCart("lisa")

  cartManagerActor ! AddItem("tom", "socks")
  cartManagerActor ! AddItem("lisa", "book")
  cartManagerActor ! AddItem("lisa", "computer")
  cartManagerActor ! AddItem("lisa", "hot dog")
  cartManagerActor ! AddItem("tom", "pen")

  cartManagerActor ! CheckOut("tom")

  StdIn.readLine()
  system.terminate()

}
