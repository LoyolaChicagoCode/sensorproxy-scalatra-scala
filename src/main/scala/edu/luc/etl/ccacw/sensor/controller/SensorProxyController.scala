package edu.luc.etl.ccacw.sensor
package controller

import javax.servlet.http.HttpServletRequest
import org.scalatra._
import org.scalatra.json._
import org.scalatra.swagger.Swagger
import org.scalatra.swagger.SwaggerSupport
import org.json4s.{ DefaultFormats, Formats }
import model._
import views._
import data.network
import org.json4s.Extraction
import twirl.api.Template1

// finish templates
// TODO navigation into tree
// TODO tests
// TODO mongo/salat

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

  def renderAsJsonOrHtml[T, U >: T](view: twirl.api.Template1[T, twirl.api.Html])(result: U)(implicit request: HttpServletRequest) =
    if (accept("application/json")) result else view.render(result.asInstanceOf[T])

  val getRoot = (apiOperation[String]("getRoot")
    summary "Show root"
    notes "Shows root. Doesn't do much else."
  )

  get("/", operation(getRoot)) {
    renderAsJsonOrHtml(html.index) {
      Seq("devices", "locations", "measurements") map { v => (v.capitalize, relativeUrl("/" + v)) } toMap
    }
  }

  val getDevices = (apiOperation[Seq[Device]]("getDevices")
    summary "Shows all devices"
    notes "Shows all devices. You can search them too."
  )

  get("/devices/?", operation(getDevices)) {
    renderAsJsonOrHtml(html.devices) {
      network.flatten.filter(_.isInstanceOf[Device]).asInstanceOf[Iterable[Device]]
    }
  }

  val findDeviceById = (apiOperation[Device]("findDeviceById")
    summary "Finds a device by ID"
    notes "Finds a device by ID."
    parameters (
      pathParam[String]("id").description("ID of device that needs to be found")
    )
  )

  get("/devices/:id/?", operation(findDeviceById)) {
    val id = params("id")
    renderAsJsonOrHtml(html.device) {
      {
        network.flatten find {
          n => n.isInstanceOf[Device] && n.asInstanceOf[Device].id == id
        }
      }.asInstanceOf[Option[Device]]
    }
  }

  val getMeasurements = (apiOperation[Seq[Measurement]]("getMeasurements")
    summary "Shows all measurements"
    notes "Shows all measurements. You can search them too."
  )

  get("/measurements/?", operation(getMeasurements)) {
    val result = network.flatten.filter { _.isInstanceOf[Measurement] }.map { _.name }.distinct
    if (accept("application/json"))
      result
    else
      <html>
        <body>
          <h1>/measurements/</h1>
          <ul>
            { result map { d => <li> { d } </li> } }
          </ul>
        </body>
      </html>
  }

  val findMeasurementByName = (apiOperation[Measurement]("findMeasurementByName")
    summary "Finds a measurement by name"
    notes "Finds a measurement by name."
    parameters (
      pathParam[String]("name").description("Name of measurement that needs to be found")
    )
  )

  get("/measurements/:name/?", operation(findMeasurementByName)) {
    val result = network.loc.cojoin.toTree.flatten filter {
      n => n.getLabel.isInstanceOf[Measurement] && n.getLabel.asInstanceOf[Measurement].name == params("name")
    }
    if (accept("application/json"))
      result
    else
      <html>
        <body>
          <h1>/measurements/{ params("name") }</h1>
          <ul>
            { result map { m => <li> { m.getLabel } at { m.parent.get.parent.get.getLabel } </li> } }
          </ul>
        </body>
      </html>
  }

  val getLocations = (apiOperation[Seq[Location]]("getLocations")
    summary "Shows all locations at a given level"
    notes "Shows all locations at the given level in the hierarchy." +
    " Path formats: /locations, /locations/l1/locations, ..., " +
    "/locations/l1/locations/l2/.../locations"
  )

  get("""^((?:/locations/(?:[^/?#]*?))*?/locations/?)$""".r, operation(getLocations)) {
    <html>
      <body>
        <p>{ multiParams("captures") }</p>
      </body>
    </html>
  }

  val getLocationByName = (apiOperation[Location]("getLocationByName")
    summary "Gets a location at a given level by name"
    notes "Gets a location at the given level in the hierarchy by name." +
    " Path format: /locations/l1, /locations/l1/locations/l2, ..., " +
    "/locations/l1/locations/l2/.../locations/ln"
  )

  get("""^((?:/locations/(?:[^/?#]*?))+?)/?$""".r, operation(getLocationByName)) {
    <html>
      <body>
        <p>{ multiParams("captures").head split "/" filter { _ != "locations" } }</p>
      </body>
    </html>
  }
}
