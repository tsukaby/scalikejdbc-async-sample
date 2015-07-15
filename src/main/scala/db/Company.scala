package db

import org.joda.time.DateTime
import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

case class Company(
  id: Long,
  name: String,
  employees: Seq[Person] = Nil) extends ShortenedNames

object Company extends SQLSyntaxSupport[Company] with ShortenedNames {
  override val columnNames = Seq("id", "name")

  def apply(c: SyntaxProvider[Company])(rs: WrappedResultSet): Company = apply(c.resultName)(rs)

  def apply(c: ResultName[Company])(rs: WrappedResultSet): Company = new Company(
    id = rs.long(c.id),
    name = rs.string(c.name)
  )

  lazy val c = Company.syntax("c")
  lazy val p = Person.p

  def findAll()(implicit session: AsyncDBSession, cxt: EC = ECGlobal): Future[List[Company]] = {
    withSQL {
      select
        .from[Company](Company as c)
        .leftJoin(Person as p).on(c.id, p.companyId)
    }.one(Company(c))
      .toMany(Person.opt(p))
      .map { (company, persons) => company.copy(employees = persons) }
      .list().future
  }
}
