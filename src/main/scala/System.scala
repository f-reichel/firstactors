import actor.{CartManagerActor, FirstActor, SayHello}
import akka.actor.{ActorRef, ActorSystem, Props}
import message.{AddItem, CreateCart}

import scala.io.StdIn
import scala.concurrent.duration._

object System extends App {
  println("Starting up my first actor system")

  val system = ActorSystem("my-first-system")

  val firstActor: ActorRef = system.actorOf(Props[FirstActor], "first-actor")
  val cartManagerActor = system.actorOf(Props[CartManagerActor])

  firstActor ! SayHello("Michael")
  firstActor ! SayHello("Lisa")
  firstActor ! "This is a String message"
  firstActor ! SayHello("Tom")

  cartManagerActor ! CreateCart("tom")
  cartManagerActor ! CreateCart("lisa")

  cartManagerActor ! AddItem("tom", "socks")
  cartManagerActor ! AddItem("lisa", "book")
  cartManagerActor ! AddItem("lisa", "computer")
  cartManagerActor ! AddItem("lisa", "hot dog")
  cartManagerActor ! AddItem("tom", "pen")

  StdIn.readLine()
  system.terminate()

}
