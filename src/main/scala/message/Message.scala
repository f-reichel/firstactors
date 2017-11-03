package message

trait Message

sealed case class CreateCart(forId: String) extends Message
sealed case class AddItem(toCart: String, item: String) extends Message


trait EventMessage extends Message

sealed case class AddedItemEvent(item:String) extends EventMessage