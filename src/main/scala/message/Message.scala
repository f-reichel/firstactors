package message

trait Message

sealed case class CreateCart(forId: String) extends Message
sealed case class AddItem(toCart: String, item: String) extends Message

sealed case class CheckOut(cartId: String) extends Message
sealed case class CollectPayment(price: Double, accountId: String, text: String) extends Message
sealed case class Deliver(items: Map[String, Int], address: String)
sealed case class SendConfirmation(to: String, msg: String)


trait EventMessage extends Message

sealed case class AddedItemEvent(item:String) extends EventMessage
sealed case class CheckedOutEvent(paymentId: String, trackingId: String) extends EventMessage
sealed case class PaymentCollectedEvent(transactionId: String) extends EventMessage
sealed case class DeliveredEvent(trackingId: String) extends EventMessage
sealed case class SentConfirmationEvent() extends EventMessage