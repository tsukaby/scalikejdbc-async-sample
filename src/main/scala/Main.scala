import db.Company
import scalikejdbc.{LoggingSQLAndTimeSettings, GlobalSettings, ConnectionPool}
import scalikejdbc.async._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

object Main extends App {
  AsyncConnectionPool.singleton("jdbc:mysql://localhost:3306/foo", "root", "", AsyncConnectionPoolSettings(maxQueueSize = 100))
  // ConnectionPool.singleton("jdbc:mysql://localhost:3306/foo", "root", "")

  GlobalSettings.loggingSQLAndTime = LoggingSQLAndTimeSettings(enabled = true, singleLineMode = true)

  val futures = (0 until 100).map { x =>
    AsyncDB.localTx { implicit tx =>
      for {
        companies <- Company.findAll
      } yield companies
    }
  }

  val res: Seq[List[Company]] = Await.result(Future.sequence(futures), Duration.Inf)

  res.foreach(x => {
    println(x.length)
    x.foreach(y => println(y.employees.length))
  })
}