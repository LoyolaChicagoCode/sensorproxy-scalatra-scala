import edu.luc.etl.scalatra.hello._
import org.scalatra._
import javax.servlet.ServletContext
import org.scalatra.swagger._
import edu.luc.etl.scalatra.hello._

class ScalatraBootstrap extends LifeCycle {
  implicit val _ = new MyScalatraSwagger
  override def init(context: ServletContext) {
    context.mount(new MyScalatraServlet, "/*")
    context mount(new ResourcesApp, "/api-docs/*")
  }
}

