package edu.luc.etl.scalatra.hello

import org.scalatra._
import scalate.ScalateSupport
import org.scalatra.swagger.Swagger
import org.scalatra.json.NativeJsonSupport
import org.scalatra.swagger.SwaggerSupport
import org.json4s.{DefaultFormats, Formats}

class MyScalatraServlet(implicit val swagger: Swagger) extends MyScalatraWebAppStack with NativeJsonSupport with SwaggerSupport {

  implicit override val jsonFormats: Formats = DefaultFormats

  override protected val applicationName = Some("hello")

  protected val applicationDescription = "The hello API. It exposes hello."

  val getRoot =
    (apiOperation[String]("getRoot")
      summary "Show hello"
      notes "Shows hello. You cannot search it either.")

  get("/", operation(getRoot)) {
    <html>
      <body>
        <h1>Hello, world!</h1>
        Say <a href="hello-scalate">hello to Scalate</a>.
      </body>
    </html>
  }

}
