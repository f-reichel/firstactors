package actor

import java.util.UUID

import akka.actor.{Actor, ActorLogging}
import message.{CollectPayment, PaymentCollectedEvent}

class PaymentActor extends Actor with ActorLogging {


  override def receive = {
    case CollectPayment(price, accountId, text) =>
      // API call to PayPal or master card here...
      log.info(s"will collect $price for $accountId with message $text")
      sender() ! PaymentCollectedEvent(UUID.randomUUID().toString)
  }

}
