import actor._
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import message.{AddItem, CheckOut, CreateCart}

import scala.concurrent.Future
import scala.io.StdIn
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

object System extends App {
  println("Starting up my first actor system")

  implicit val system = ActorSystem("my-first-system")
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher

  val port = 8087
  val host = "localhost"

  val firstActor: ActorRef = system.actorOf(Props[FirstActor], "first-actor")
  val cartManagerActor = system.actorOf(Props[CartManagerActor], "cart-manager")

  system.actorOf(Props[PaymentActor], "payment-actor")
  system.actorOf(Props[DeliveryActor], "delivery-actor")
  system.actorOf(Props[CommunicationsActor], "communications-actor")

  // configuration  verb+path to "method" table
  val paths =
    pathPrefix("api" / "v1") {
      path("cart") {
        get {
          // function is called when receiving path "/api/v1/cart" with GET
          complete("hello world")
        }
      }
    }

  val webserverHandle : Future[Http.ServerBinding] = Http().bindAndHandle(paths, host, port)

  /*
  cartManagerActor ! CreateCart("tom")
  cartManagerActor ! CreateCart("lisa")

  cartManagerActor ! AddItem("tom", "socks")
  cartManagerActor ! AddItem("lisa", "book")
  cartManagerActor ! AddItem("lisa", "computer")
  cartManagerActor ! AddItem("lisa", "hot dog")
  cartManagerActor ! AddItem("tom", "pen")

  cartManagerActor ! CheckOut("tom")
*/

  println(s"Webserver started, listening on $host:$port")
  println(s"Press any key to shutdown")

  StdIn.readLine()
  webserverHandle.flatMap( _.unbind )
  system.terminate()

}
