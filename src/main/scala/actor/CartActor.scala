package actor

import akka.actor.Actor
import message.AddItem

class CartActor(var id:String) extends Actor {

  println(s"CartActor($id) created")

  override def receive = {
    case AddItem(id,item) => println(s"adding $item to cart $id")
  }
}
