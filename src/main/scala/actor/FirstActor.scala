package actor

import akka.actor.Actor

class FirstActor extends Actor {

  override def receive = {
    case SayHello(name) => println(s"Hi $name")
    case _ => println("Don't know what you mean?!?")
  }
}

case class SayHello(toName: String)
