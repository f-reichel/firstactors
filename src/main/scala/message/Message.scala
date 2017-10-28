package message

trait Message

sealed case class CreateCart(forId: String) extends Message
sealed case class AddItem(toCart: String, item: String) extends Message