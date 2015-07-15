package db

import scalikejdbc._
import scalikejdbc.async._

case class Person(
  id: Long,
  companyId: Long,
  name: String
) extends ShortenedNames

object Person extends SQLSyntaxSupport[Person] with ShortenedNames {
  override val columnNames = Seq("id", "company_id", "name")

  def apply(p: SyntaxProvider[Person])(rs: WrappedResultSet): Person = apply(p.resultName)(rs)

  def apply(p: ResultName[Person])(rs: WrappedResultSet): Person = new Person(
    id = rs.long(p.id),
    companyId = rs.long(p.companyId),
    name = rs.string(p.name)
  )

  def opt(p: SyntaxProvider[Person])(rs: WrappedResultSet): Option[Person] = {
    rs.longOpt(p.resultName.id).map(_ => Person(p)(rs))
  }

  lazy val p = Person.syntax("p")
}