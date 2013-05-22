package edu.luc.etl.ccacw.sensor

import scalaz._
import Scalaz._
import model._

package object data {

  // TODO DRY/factories for devices and other common subtrees

  val network: Tree[Resource] =
    Location("luc").node(
      Location("lsc").node(
        Location("cuneo").node(
          (new Device(name = "42i", id = "00:11:22:33:44:01", address = "localhost:9501")).node(
            ModbusSetting(name = "unit", offset = 3).node(
              SettingValue(name = "false", value = "ppb").leaf,
              SettingValue(name = "true", value = "ug/m3").leaf
            ),
            Measurement("no").node(
              Reading(name = "current", offset = 0).leaf,
              Reading(name = "min", offset = 10).leaf,
              Reading(name = "max", offset = 20).leaf
            ),
            Measurement("no2").node(
              Reading(name = "current", offset = 2).leaf,
              Reading(name = "min", offset = 12).leaf,
              Reading(name = "max", offset = 22).leaf
            ),
            Measurement("nox").node(
              Reading(name = "current", offset = 4).leaf,
              Reading(name = "min", offset = 14).leaf,
              Reading(name = "max", offset = 24).leaf
            )
          ),
          (new Device(name = "49i", id = "00:11:22:33:44:02", address = "localhost:9502")).node(
            ModbusSetting(name = "unit", offset = 2).node(
              SettingValue(name = "false", value = "ppb").leaf,
              SettingValue(name = "true", value = "ug/m3").leaf
            ),
            Measurement("o3").node(
              Reading(name = "current", offset = 0).leaf,
              Reading(name = "min", offset = 10).leaf,
              Reading(name = "max", offset = 20).leaf
            )
          )
        ),
        Location("damen").node(
          (new Device(name = "49i", id = "00:11:22:33:44:03", address = "localhost:9503")).node(
            ModbusSetting(name = "unit", offset = 2).node(
              SettingValue(name = "false", value = "ppb").leaf,
              SettingValue(name = "true", value = "ug/m3").leaf
            ),
            Measurement("o3").node(
              Reading(name = "current", offset = 0).leaf,
              Reading(name = "min", offset = 10).leaf,
              Reading(name = "max", offset = 20).leaf
            )
          )
        )
      ),
      Location("wtc").node(
        Location("baumhart").node(
          Location("rooftop").node(
            (new Device(name = "42i", id = "00:11:22:33:44:04", address = "localhost:9504")).node(
              ModbusSetting(name = "unit", offset = 3).node(
                SettingValue(name = "false", value = "ppb").leaf,
                SettingValue(name = "true", value = "ug/m3").leaf
              ),
              Measurement("no").node(
                Reading(name = "current", offset = 0).leaf,
                Reading(name = "min", offset = 10).leaf,
                Reading(name = "max", offset = 20).leaf
              ),
              Measurement("no2").node(
                Reading(name = "current", offset = 2).leaf,
                Reading(name = "min", offset = 12).leaf,
                Reading(name = "max", offset = 22).leaf
              ),
              Measurement("nox").node(
                Reading(name = "current", offset = 4).leaf,
                Reading(name = "min", offset = 14).leaf,
                Reading(name = "max", offset = 24).leaf
              )
            ),
            (new Device(name = "49i", id = "00:11:22:33:44:05", address = "localhost:9505")).node(
              ModbusSetting(name = "unit", offset = 2).node(
                SettingValue(name = "false", value = "ppb").leaf,
                SettingValue(name = "true", value = "ug/m3").leaf
              ),
              Measurement("o3").node(
                Reading(name = "current", offset = 0).leaf,
                Reading(name = "min", offset = 10).leaf,
                Reading(name = "max", offset = 20).leaf
              )
            )
          ),
          Location("basement").node(
            (new Device(name = "49i", id = "00:11:22:33:44:06", address = "localhost:9506")).node(
              ModbusSetting(name = "unit", offset = 2).node(
                SettingValue(name = "false", value = "ppb").leaf,
                SettingValue(name = "true", value = "ug/m3").leaf
              ),
              Measurement("o3").node(
                Reading(name = "current", offset = 0).leaf,
                Reading(name = "min", offset = 10).leaf,
                Reading(name = "max", offset = 20).leaf
              )
            )
          )
        )
      ),
      Location("malibu").node(
        (new Device(name = "49i", id = "00:11:22:33:44:06", address = "localhost:9506")).node(
          ModbusSetting(name = "unit", offset = 2).node(
            SettingValue(name = "false", value = "ppb").leaf,
            SettingValue(name = "true", value = "ug/m3").leaf
          ),
          Measurement("o3").node(
            Reading(name = "current", offset = 0).leaf,
            Reading(name = "min", offset = 10).leaf,
            Reading(name = "max", offset = 20).leaf
          )
        )
      )
    )
}