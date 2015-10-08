package controllers

import javax.inject.Inject

import play.api._
import play.api.mvc._
import dao.RecetaDAO
import models.Receta
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.text
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Controller
import views.html.helper.form

import scala.concurrent.Future

class Application @Inject()(dao: RecetaDAO) extends Controller {

  def index = Action.async {
    val listaReceta: Future[List[Receta]] = dao.all()
    listaReceta.map(c => Ok(views.html.index(c)))
  }

  private val recetaForm = Form(
    mapping(
      "nombre" -> text,
      "ingredientes" -> text,
      "instrucciones" -> text,
      "imagen" -> text
    )(Receta.apply)(Receta.unapply)
  )

  def agregar = Action {
    Ok(views.html.recetaForm(recetaForm))
  }
  def insert = Action.async { implicit request =>
    val r: Receta = recetaForm.bindFromRequest.get
    dao.insert(r).map(_ => Redirect(routes.Application.index))
  }

  def getJson = TODO
}
