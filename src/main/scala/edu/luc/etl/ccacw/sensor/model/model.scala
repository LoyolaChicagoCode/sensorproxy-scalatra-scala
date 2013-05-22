package edu.luc.etl.ccacw.sensor.model

// TODO factories/validation?

trait Resource {
  def name: String
}

trait HasReading {
  def read(m: Measurement, r: Reading): Float
}

trait HasFakeReading extends HasReading {
  override def read(m: Measurement, r: Reading) = scala.math.random.toFloat
}

case class Location(name: String) extends Resource
case class Device(name: String, id: String, address: String) extends Resource
case class Measurement(name: String) extends Resource
case class Reading(name: String, offset: Int) extends Resource
case class ModbusSetting(name: String, offset: Int) extends Resource
case class SettingValue(name: String, value: String) extends Resource