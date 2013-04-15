package edu.luc.etl.ccacw.sensor
package service

import javax.servlet.http.HttpServletRequest
import org.scalatra._
import org.scalatra.json._
import org.scalatra.swagger.Swagger
import org.scalatra.swagger.SwaggerSupport
import org.scalatra.json.JacksonJsonSupport
import org.json4s.{ DefaultFormats, Formats }
import domain.model._
import domain.instance.network

class SensorProxyController(implicit val swagger: Swagger) extends SensorProxyWebAppStack
    with JacksonJsonSupport with SwaggerSupport with HALBuilderSupport with ApiVersion {

  implicit override val jsonFormats: Formats = DefaultFormats

  override protected val applicationName = Some("sensorproxy-scalatra-scala")

  protected val applicationDescription = "The hello API. It exposes hello."

  def accept(mediaTypes: String*)(implicit request: HttpServletRequest) = {
    val accept = request.getHeader("Accept")
    mediaTypes.exists { accept.contains(_) }
  }

  val getRoot = (apiOperation[String]("getRoot")
    summary "Show root"
    notes "Shows root. Doesn't do much else."
  )

  get("/", operation(getRoot)) {
    if (accept("text/html")) {
      // TODO separate template
      <html>
        <head>
          <title>Loyola ETL/CUERP CCACW Sensor Proxy Demo</title>
        </head>
        <body>
          <h1>Loyola ETL/CUERP CCACW Sensor Proxy Demo</h1>
          <ul>
            <li><a href={ version + "/devices" }>Devices</a></li>
            <li><a href={ version + "/locations" }>Locations</a></li>
            <li><a href={ version + "/measurements" }>Measurements</a></li>
          </ul>
        </body>
      </html>
    }
    else {
      val rf = representationFactory
      val rep = rf.newRepresentation("/" + version)
        .withLink("devices", "/devices")
        .withLink("measurements", "/measurements")
        .withLink("locations", "/locations")
      rep.toString(if (accept("application/json")) "application/json" else "application/xml")
    }
  }

  val getDevices = (apiOperation[Seq[Device]]("getDevices")
    summary "Shows all devices"
    notes "Shows all devices. You can search them too."
  )

  get("/devices/?", operation(getDevices)) {
    <html>
      <body>
        <h1>/devices/</h1>
        <ul>
          { network.flatten filter { _.isInstanceOf[Device] } map { d => <li> { d } </li> } }
        </ul>
      </body>
    </html>
  }

  get("/devices/?", accept("application/json"), operation(getDevices)) {
    network.flatten filter { _.isInstanceOf[Device] }
  }

  val findDeviceById = (apiOperation[Device]("findDeviceById")
    summary "Finds a device by ID"
    notes "Finds a device by ID."
    parameters (
      pathParam[String]("id").description("ID of device that needs to be found")
    )
  )

  get("/devices/:id/?", operation(findDeviceById)) {
    <html>
      <body>
        <h1>/devices/{ params("id") }</h1>
        <p>
          { network.flatten find { n => n.isInstanceOf[Device] && n.asInstanceOf[Device].id == params("id") } }
        </p>
      </body>
    </html>
  }

  get("/devices/:id/?", accept("application/json"), operation(findDeviceById)) {
    //    contentType = formats("json")
    // this works
    Some(Device("lkj", "ouoiu", "oiuoiu"))
    // this doesn't
    //    { network.flatten find { n => n.isInstanceOf[Device] && n.asInstanceOf[Device].id == params("id") } }.asInstanceOf[Option[Seq[Device]]]
  }

  val getMeasurements = (apiOperation[Seq[Measurement]]("getMeasurements")
    summary "Shows all measurements"
    notes "Shows all measurements. You can search them too."
  )

  get("/measurements/?", operation(getMeasurements)) {
    <html>
      <body>
        <h1>/measurements/</h1>
        <ul>
          { network.flatten.filter { _.isInstanceOf[Measurement] }.map { _.name }.distinct map { d => <li> { d } </li> } }
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
    <html>
      <body>
        <h1>/measurements/{ params("name") }</h1>
        <ul>
          {
            network.loc.cojoin.toTree.flatten.filter {
              n => n.getLabel.isInstanceOf[Measurement] && n.getLabel.asInstanceOf[Measurement].name == params("name")
            }.map {
              m => <li> { m.getLabel } at { m.parent.get.parent.get.getLabel } </li>
            }
          }
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
