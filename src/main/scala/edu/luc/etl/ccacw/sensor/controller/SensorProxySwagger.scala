package edu.luc.etl.ccacw.sensor.controller

import org.scalatra.swagger.{ JacksonSwaggerBase, Swagger }
import org.scalatra.ScalatraServlet
import org.json4s.{ DefaultFormats, Formats }

class ResourcesApp(implicit val swagger: Swagger)
    extends ScalatraServlet with JacksonSwaggerBase {
  implicit override val jsonFormats: Formats = DefaultFormats
}

class SensorProxySwagger extends Swagger("1.0", "1")