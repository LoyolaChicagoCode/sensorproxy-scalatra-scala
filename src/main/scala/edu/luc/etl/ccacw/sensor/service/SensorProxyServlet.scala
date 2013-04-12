package edu.luc.etl.ccacw.sensor
package service

import org.scalatra._
import scalate.ScalateSupport
import org.scalatra.swagger.Swagger
import org.scalatra.json.NativeJsonSupport
import org.scalatra.swagger.SwaggerSupport
import org.json4s.{ DefaultFormats, Formats }
import domain.model._
import domain.instance.network

// TODO JSON

class SensorProxyServlet(implicit val swagger: Swagger)
  extends SensorProxyWebAppStack
  with NativeJsonSupport
  with SwaggerSupport {

  implicit override val jsonFormats: Formats = DefaultFormats

  override protected val applicationName = Some("sensorproxy-scalatra-scala")

  protected val applicationDescription = "The hello API. It exposes hello."

  val getRoot = (apiOperation[String]("getRoot")
    summary "Show root"
    notes "Shows root. Doesn't do much."
  )

//  before() {
//    contentType = formats("json")
//  }

  get("/", operation(getRoot)) {
    val version = "v1alpha" // TODO DRY (reverse URL?!?)
    contentType = formats("html")
	<html>
	  <head>
	    <title>Loyola ETL/CUERP CCACW Sensor Proxy Demo</title>
	  </head>
	  <body>
	  <h1>Loyola ETL/CUERP CCACW Sensor Proxy Demo</h1>
	    <ul>
	      <li><a href={ version + "/devices"      }>Devices</a></li>
	      <li><a href={ version + "/locations"    }>Locations</a></li>
	      <li><a href={ version + "/measurements" }>Measurements</a></li>
	    </ul>
	  </body>
	</html>
  }

  val getDevices = (apiOperation[String]("getDevices") // TODO fix response type
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

  val findDeviceById = (apiOperation[String]("findDeviceById") // TODO fix response type
    summary "Finds a device by ID"
    notes "Finds a device by ID."
    parameters (
      pathParam[String]("id").description("ID of device that needs to be found")
    )
  )

  get("/devices/:id/?", operation(findDeviceById)) {
    <html>
      <body>
        <h1>/devices/{params("id")}</h1>
        <p>
	    { network.flatten find { n => n.isInstanceOf[Device] && n.asInstanceOf[Device].id == params("id") } }
        </p>
      </body>
    </html>
  }

  val getMeasurements = (apiOperation[String]("getMeasurements") // TODO fix response type
    summary "Shows all measurements"
    notes "Shows all measurements. You can search them too."
  )

  get("/measurements/?", operation(getMeasurements)) {
    <html>
      <body>
        <h1>/measurements/</h1>
        <ul>
	    { network.flatten.filter { _.isInstanceOf[Measurement] }. map { _.name }.distinct map { d => <li> { d } </li> } }
	    </ul>
      </body>
    </html>
  }

  val findMeasurementByName = (apiOperation[String]("findMeasurementByName") // TODO fix response type
    summary "Finds a measurement by name"
    notes "Finds a measurement by name."
    parameters (
      pathParam[String]("name").description("Name of measurement that needs to be found")
    )
  )

  get("/measurements/:name/?", operation(findMeasurementByName)) {
    <html>
      <body>
        <h1>/measurements/{params("name")}</h1>
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

  val getLocations = (apiOperation[String]("getLocations") // TODO fix response type
    summary "Shows all locations"
    notes "Shows all locations. You can search them too."
  )

  get("""^((?:/locations/(?:[^/?#]*?))*?/locations/?)$""".r, operation(getLocations)) {
    <html>
      <body>
        <p>{multiParams("captures")}</p>
      </body>
    </html>
  }

  val getLocationByName = (apiOperation[String]("getLocationByName") // TODO fix response type
    summary "Gets a location by name"
    notes "Gets a location by name."
  )

  get("""^((?:/locations/(?:[^/?#]*?))+?)/?$""".r, operation(getLocationByName)) {
    <html>
      <body>
        <p>{multiParams("captures").head split "/" filter { _ != "locations" }}</p>
      </body>
    </html>
  }
}
