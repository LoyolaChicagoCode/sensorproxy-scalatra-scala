import org.eclipse.jetty.server.Server
import org.eclipse.jetty.servlet.{ DefaultServlet, ServletContextHandler }
import org.eclipse.jetty.webapp.WebAppContext
import scala.util.Properties
import edu.luc.etl.scalatra.hello._
import org.eclipse.jetty.servlet.ServletHolder

object JettyLauncher {
  def main(args: Array[String]) {
    val port = Properties.envOrElse("PORT", "8080").toInt
    val server = new Server(port)
    val context = new WebAppContext

    implicit val swagger = new MyScalatraSwagger

    context setContextPath "/"
    context.setResourceBase("src/main/webapp")
    context.addServlet(new ServletHolder(new MyScalatraServlet), "/*")
    context.addServlet(new ServletHolder(new ResourcesApp), "/api-docs/*")

    Thread.sleep(3)

    server.setHandler(context)

    Thread.sleep(3)

    server.start
    server.join
  }
}