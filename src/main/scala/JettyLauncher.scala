import scala.util.Properties
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ DefaultServlet, ServletContextHandler }
import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.servlet.ServletHolder
import edu.luc.etl.ccacw.sensor.controller._

object JettyLauncher extends ApiVersion {
  def main(args: Array[String]) {
    val port = Properties.envOrElse("PORT", "8080").toInt
    val server = new Server(port)
    val context = new WebAppContext

    context.setContextPath("/")
    context.setResourceBase("src/main/webapp")

    server.setHandler(context)

    Thread.sleep(5)

    server.start
    server.join
  }
}