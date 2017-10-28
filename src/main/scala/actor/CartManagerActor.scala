package actor

import akka.actor.{Actor, Props}
import message.{AddItem, CreateCart}

class CartManagerActor extends Actor {

  override def receive = {

    case CreateCart(id) => context.actorOf(Props(classOf[CartActor], id), "cart-"+id)

    case msg @ AddItem(id, item) =>
      val childRef = context.child("cart-"+id)
      childRef match {
        case Some(cardRef) => cardRef ! msg
        case None => println(s"no such cart (yet) with id $id")
      }
  }
}
