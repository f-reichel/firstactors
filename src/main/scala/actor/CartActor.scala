package actor

import akka.actor.Actor
import message.{AddItem, AddedItemEvent}

class CartActor(var id:String) extends Actor {

  var content = Map[String, Int]()

  println(s"CartActor($id) created")

  override def receive = {
    case AddItem(id,item) =>
      println(s"adding $item to cart of $id")
      content = content + (item -> 1)
      println(s"cart of $id is no containing: $content")
      sender() ! AddedItemEvent(item)
  }
}
