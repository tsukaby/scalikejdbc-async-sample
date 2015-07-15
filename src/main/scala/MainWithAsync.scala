import java.util.concurrent.TimeUnit
import akka.actor.{Props, ActorSystem, Actor}
import akka.pattern._
import akka.util.Timeout
import org.joda.time.DateTime
import programmerlist.Company
import scalikejdbc.async.ShortenedNames._
import scalikejdbc.async._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._
object MainWithAsync extends App {
  AsyncConnectionPool.singleton("jdbc:mysql://localhost:3306/foo", "root", "")

  // normalCreate 公式サンプルの例。正しくsession維持される。Actorではない。
  // akkaCreateXuweikVer // Actor版　吉田さん指摘により正しく動くようになった。
  akkaCreateGakuzzzzVer // Actor版　がくぞさん指摘取り込み txを使うのが良さそう？

    private def normalCreate = {
    // create a new record within a transaction
    val created: Future[Company] = AsyncDB.localTx { implicit tx =>
      for {
        company <- Company.create("ScalikeJDBC, Inc." + DateTime.now.getMillis, Some("http://scalikejdbc.org/"))
        company <- Company.create("ScalikeJDBC, Inc." + DateTime.now.getMillis, Some("http://scalikejdbc.org/"))
        company <- Company.create("ScalikeJDBC, Inc." + DateTime.now.getMillis, Some("http://scalikejdbc.org/"))
        company <- Company.create("ScalikeJDBC, Inc." + DateTime.now.getMillis, Some("http://scalikejdbc.org/"))
        company <- Company.create("ScalikeJDBC, Inc." + DateTime.now.getMillis, Some("http://scalikejdbc.org/"))
      } yield company
    }
    Await.result(created, 5.seconds)
  }

  private def akkaCreateXuweikVer = {
    implicit val timeout = Timeout(60, TimeUnit.SECONDS)
    val system = ActorSystem("mySystem")
    val cActor = system.actorOf(Props[CompanyActor], "cActor")
    val tx = AsyncDB.localTx { implicit tx =>

      for {
        c <- cActor.ask(Message("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
        c <- cActor.ask(Message("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
        c <- cActor.ask(Message("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
        c <- cActor.ask(Message("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
        c <- cActor.ask(Message("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
      } yield()
    }
    try Await.result(tx, 5.seconds)
    catch { case e:Exception => println(e.getMessage)}
    system.shutdown()
  }

  private def akkaCreateGakuzzzzVer = {
    implicit val timeout = Timeout(60, TimeUnit.SECONDS)
    val system = ActorSystem("mySystem")
    val cActor = system.actorOf(Props[CompanyActor], "cActor")
    val tx = AsyncDB.localTx { implicit tx =>

      for {
        c <- cActor.ask(MessageWithGakuzzzz("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
        c <- cActor.ask(MessageWithGakuzzzz("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
        c <- cActor.ask(MessageWithGakuzzzz("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
        c <- cActor.ask(MessageWithGakuzzzz("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
        c <- cActor.ask(MessageWithGakuzzzz("ScalikeJDBC, Inc." + DateTime.now.getMillis, tx))
      } yield()
    }

    try Await.result(tx, 5.seconds)

    catch { case e:Exception => println(e.getMessage)}
    system.shutdown()
  }
}
final case class Message(name: String, tx: TxAsyncDBSession)
final case class MessageWithGakuzzzz(name: String, tx: TxAsyncDBSession)

class CompanyActor extends Actor {
  def receive = {
    case Message(name, tx) =>
      Company.create(name, Some("http://scalikejdbc.org/"))(tx, implicitly).pipeTo(sender())
    case MessageWithGakuzzzz(name, tx) =>
      Company.create(name, Some("http://scalikejdbc.org/"))(tx, implicitly).pipeTo(sender())
  }
}
