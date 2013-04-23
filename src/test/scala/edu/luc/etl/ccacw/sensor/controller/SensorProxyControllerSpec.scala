package edu.luc.etl.ccacw.sensor.controller

import org.scalatra.test.specs2._

// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
class SensorProxyControllerSpec extends ScalatraSpec with ApiVersion {
  def is =
    "GET / on SensorProxyController" ^
      "should return status 200" ! root200 ^
      end

  addServlet(new SensorProxyController, "/" + version + "/*")

  def root200 = get("/") {
    status must_== 200
  }

  implicit lazy val _ = new SensorProxySwagger
}
