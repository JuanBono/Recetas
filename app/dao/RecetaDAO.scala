package dao


import scala.concurrent.Future

import models.Receta
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfig
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

class RecetaDAO extends HasDatabaseConfig[JdbcProfile] {
  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  private val Recetas = TableQuery[RecetasTable]

  def all(): Future[List[Receta]] = db.run(Recetas.result).map(_.toList)

  def insert(r: Receta): Future[Unit] = db.run(Recetas += r).map(_ => ())

  private class RecetasTable(tag: Tag) extends Table[Receta](tag, "RECETA") {

    def nombre = column[String]("NOMBRE", O.PrimaryKey)

    def ingredientes = column[String]("INGREDIENTES")

    def instrucciones = column[String]("INSTRUCCIONES")

    def imagen = column[String]("IMAGEN")


    def * = (nombre, ingredientes, instrucciones, imagen) <>(Receta.tupled, Receta.unapply _)
  }

}