import actor._
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import message._

import scala.concurrent.Future
import scala.io.StdIn
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol
import akka.pattern.ask
import akka.util.Timeout

import scala.util.{Failure, Random, Success}
import scala.concurrent.duration._

object System extends App with DefaultJsonProtocol with SprayJsonSupport {
  println("Starting up my first actor system")

  implicit val system = ActorSystem("my-first-system")
  implicit val materializer = ActorMaterializer()
  implicit val dispatcher = system.dispatcher
  implicit val timeout = Timeout(10 seconds)

  implicit val addItemFormat = jsonFormat2(AddItem)
  implicit val addedItemEventFormat = jsonFormat1(AddedItemEvent)
  implicit val checkedOutEventFormat = jsonFormat2(CheckedOutEvent)

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
      path("hello") {
        get {
          // function is called when receiving path "/api/v1/cart" with GET
          complete("hello world")
        }
      } ~
      path("cart" / Segment / "item") { userName => { // userName is taken from path (from variable "Segment")
          post {
            entity(as[AddItem]) { (addedItem) =>
              onComplete((cartManagerActor ? addedItem.copy(toCart = userName)).mapTo[AddedItemEvent]) {
                case Success(e)  => complete(201, e) // 201=CREATED
                case Failure(ex) => complete(503, ex.getMessage) // 503=SERVICE UNAVAILABLE
              }
            }
          }
        }
      } ~
      path("cart" / Segment) { userName => // userName is taken from path (from variable "Segment")
          post {
            cartManagerActor ! CreateCart(userName)
            // no onCompelte with future here since only telling not asking (no reply from managing actor)
            complete(201, userName) // 201=CREATED
          }
      } ~
      path("cart" / Segment) { userName => {
          delete { // delete used as "checking out"
            onComplete((cartManagerActor ? CheckOut(userName)).mapTo[AddedItemEvent]) {
              case Success(e) => complete(200, e) // 200=OK
              case Failure(ex) => complete(503, ex.getMessage) // 503=SERVICE UNAVAILABLE
            }
          }
        }
      }
    }


  val webserverHandle : Future[Http.ServerBinding] = Http().bindAndHandle(paths, host, port)


  println(s"Webserver started, listening on $host:$port")
  println(s"Press any key to shutdown")

  StdIn.readLine()
  webserverHandle.flatMap( _.unbind )
  system.terminate()

}
