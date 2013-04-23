package edu.luc.etl.ccacw.sensor.controller

import com.theoryinpractise.halbuilder.api.RepresentationFactory
import com.theoryinpractise.halbuilder.DefaultRepresentationFactory
import com.theoryinpractise.halbuilder.impl.json.JsonRepresentationWriter
import com.theoryinpractise.halbuilder.api.RepresentationWriter
import com.theoryinpractise.halbuilder.impl.xml.XmlRepresentationWriter

trait HALBuilderSupport {
  lazy val representationFactory: RepresentationFactory =
    (new DefaultRepresentationFactory).
      withRenderer("application/json", classOf[JsonRepresentationWriter[String]]).
      withRenderer("application/xml", classOf[XmlRepresentationWriter[String]])
}