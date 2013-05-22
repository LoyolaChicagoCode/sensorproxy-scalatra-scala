package edu.luc.etl.ccacw.sensor
package controller

import javax.servlet.http.HttpServletRequest
import org.scalatra._
import org.scalatra.json._
import org.scalatra.swagger.Swagger
import org.scalatra.swagger.SwaggerSupport
import org.json4s.{ DefaultFormats, Formats }
import org.json4s.Extraction
import twirl.api.Template1
import model._
import views._
import data._

// DONE finish templates
// TODO tests
// TODO mongo/salat
// TODO improve representations for demoing
// TODO support for typesafe (schema-aware) XML representations (and convert to JSON as a separate concern)
//      or take another look at JSON schema
// TODO write side of the web: device registration
// TODO distill/articulate insights on variably-composite resources
//      (as opposed to fixed resource hierarchies)
// TODO dependency injection of model
// TODO correct matching of fixed path components to associations, e.g. locations versus devices

class SensorProxyController(implicit val swagger: Swagger) extends ScalatraServlet
    with JacksonJsonSupport with JValueResult with ApiFormats
    with SwaggerSupport with ApiVersion {

  implicit override val jsonFormats: Formats = DefaultFormats

  override protected val applicationName = Some("sensorproxy-scalatra-scala")

  protected val applicationDescription = "The hello API. It exposes hello."

  def accept(mediaTypes: String*)(implicit request: HttpServletRequest) = {
    val accept = request.getHeader("Accept")
    mediaTypes.exists { accept.contains(_) }
  }

  implicit def resourceDowncast[T](r: Resource): T = r.asInstanceOf[T]

  implicit def resourceIterableDowncast[M[_], T, U >: T](m: M[U]): M[T] = m.asInstanceOf[M[T]]

  def represent[T, U >: T](view: twirl.api.Template1[T, twirl.api.Html])(result: U)(implicit request: HttpServletRequest, u2t: U => T) =
    if (accept("application/json")) result else view.render(result)

  def represent[S, T, U >: T](view: twirl.api.Template2[S, T, twirl.api.Html])(s: S)(result: U)(implicit request: HttpServletRequest, u2t: U => T) =
    if (accept("application/json")) result else view.render(s, result)

  val getRoot = (apiOperation[String]("getRoot")
    summary "Show root"
    notes "Shows root. Doesn't do much else."
  )

  get("/", operation(getRoot)) {
    represent(html.index) {
      Seq("devices", "locations", "measurements") map { v => (v.capitalize, relativeUrl("/" + v)) } toMap
    }
  }

  val getDevices = (apiOperation[Seq[Device]]("getDevices")
    summary "Shows all devices"
    notes "Shows all devices. You can search them too."
  )

  get("/devices/?", operation(getDevices)) {
    represent(html.devices) {
      network.flatten filter { _.isInstanceOf[Device] }
    }
  }

  val findDeviceById = (apiOperation[Device]("findDeviceById")
    summary "Finds a device by ID"
    notes "Finds a device by ID."
    parameters (
      pathParam[String]("id").description("ID of desired device")
    )
  )

  get("/devices/:id/?", operation(findDeviceById)) {
    val id = params("id")
    network.flatten find {
      n => n.isInstanceOf[Device] && n.asInstanceOf[Device].id == id
    } map {
      represent(html.device)(_)
    } getOrElse {
      NotFound(())
    }
  }

  val getMeasurements = (apiOperation[Seq[Measurement]]("getMeasurements")
    summary "Shows all measurements"
    notes "Shows all measurements. You can search them too."
  )

  get("/measurements/?", operation(getMeasurements)) {
    represent(html.measurementNames) {
      network.flatten filter { _.isInstanceOf[Measurement] } map { _.name } distinct
    }
  }

  val filterMeasurementsByName = (apiOperation[Measurement]("filterMeasurementsByName")
    summary "Filters measurements by name"
    notes "Filters measurements by name."
    parameters (
      pathParam[String]("name").description("Name of desired measurement")
    )
  )

  get("/measurements/:name/?", operation(filterMeasurementsByName)) {
    val name = params("name")
    represent(html.measurements)(name) {
      networkNavigable.flatten filter {
        n => n.getLabel.isInstanceOf[Measurement] && n.getLabel.asInstanceOf[Measurement].name == name
      }
    }
  }

  val getLocationByName = (apiOperation[Location]("getLocationByName")
    summary "Gets a location at a given level by name"
    notes "Gets a location at the given level in the hierarchy by name." +
    " Path format: /locations/l1, /locations/l1/locations/l2, ..., " +
    "/locations/l1/locations/l2/.../locations/ln"
  )

  get("""^((?:/locations/(?:[^/?#]*?))+?)/?$""".r, operation(getLocationByName)) {
    val path = multiParams("captures").head split "/" filter { _ != "locations" }
    val loc = descend(path.tail)(networkNavigable)
    loc map { l =>
      represent(html.location)(l.rootLabel)
    } getOrElse {
      NotFound(())
    }
  }

  val getLocations = (apiOperation[Seq[Location]]("getLocations")
    summary "Shows all locations at a given level"
    notes "Shows all locations at the given level in the hierarchy." +
    " Path formats: /locations, /locations/l1/locations, ..., " +
    "/locations/l1/locations/l2/.../locations"
  )

  // TODO consider making DRY with preceding route

  get("""^((?:/locations/(?:[^/?#]*?))*?/locations/?)$""".r, operation(getLocations)) {
    val path = multiParams("captures").head split "/" filter { _ != "locations" } toStream
    val loc = descend(path.tail)(networkNavigable)
    loc map { l =>
      represent(html.locations)(l)
    } getOrElse {
      NotFound(())
    }
  }
}
