package programmerlist

import org.joda.time.DateTime
import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

case class Person(
  id: Long,
  name: String
) extends ShortenedNames

object Person extends SQLSyntaxSupport[Person] with ShortendNames {
  def apply(c: SyntaxProvider[Person])(rs: WrappedResultSet): Person = apply(c.resultName)(rs)

  def apply(c: ResultName[Person])(rs: WrappedResultSet): Person = new Person(
    id = rs.long(c.id),
    name = rs.string(c.name)
  )

  lazy val p = Person.syntax("p")

  def create(name: String)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Person] = {
    for {

      id <- withSQL {
        insert.into(Person).namedValues(
          column.name -> name
      }.updateAndReturnGeneratedKey()

    } yield Person(id = id, name = name)
  }
}