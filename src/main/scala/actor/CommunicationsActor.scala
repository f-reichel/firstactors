package actor

import akka.actor.{Actor, ActorLogging}
import message.{SendConfirmation, SentConfirmationEvent}

class CommunicationsActor extends Actor with ActorLogging {

  override def receive = {
    case SendConfirmation(to, msg) =>
      log.info(s"will send message '$msg' to $to")
      sender() ! SentConfirmationEvent()
  }

}
