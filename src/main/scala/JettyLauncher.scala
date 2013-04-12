import scala.util.Properties
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ DefaultServlet, ServletContextHandler }
import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.servlet.ServletHolder
import edu.luc.etl.ccacw.sensor.service._

object JettyLauncher extends ApiVersion {
  def main(args: Array[String]) {
    val port = Properties.envOrElse("PORT", "8080").toInt
    val server = new Server(port)
    val context = new WebAppContext

    implicit val swagger = new SensorProxySwagger

    context.setContextPath(version)
    context.setResourceBase("src/main/webapp")
    context.addServlet(new ServletHolder(new SensorProxyServlet), "/*")
    context.addServlet(new ServletHolder(new ResourcesApp), "/api-docs/*")

    server.setHandler(context)

    Thread.sleep(5)

    server.start
    server.join
  }
}