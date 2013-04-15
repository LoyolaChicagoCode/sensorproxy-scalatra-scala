import javax.servlet.ServletContext
import org.scalatra._
import org.scalatra.swagger._
import edu.luc.etl.ccacw.sensor.service._

class ScalatraBootstrap extends LifeCycle with ApiVersion {
  implicit val _ = new SensorProxySwagger
  override def init(context: ServletContext) {
    context.mount(new SensorProxyController, "/" + version + "/*")
    context mount (new ResourcesApp, "/" + version + "/api-docs/*")
  }
}
