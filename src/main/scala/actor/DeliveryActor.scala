package actor

import akka.actor.{Actor, ActorLogging}
import message.{Deliver, DeliveredEvent}

import scala.util.Random

class DeliveryActor extends Actor with ActorLogging {

  override def receive = {
    case Deliver(items, address) =>
      log.info(s"will deliver $items to $address")
      sender() ! DeliveredEvent( (new Random().nextInt(899999)+100000).toString )
  }
}
