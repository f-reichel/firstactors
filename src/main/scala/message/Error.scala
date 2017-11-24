package message

trait Error

// Exceptions have to be case classes in order to be used in case statements!
case class CartStateException(msg: String) extends Exception(msg)
