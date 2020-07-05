package com.pklos.myweather.utils

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.childInt
import com.gitlab.mvysny.konsumexml.konsumeXml
import java.io.File


class YrXmlParser {
    data class Forecast(val tabular : Tabular){
        companion object {
            fun parseForecast(forecast : Konsumer) : Forecast{
                forecast.checkCurrent("forecast")
                return Forecast(forecast.child("tabular") { Tabular.parseTabular(this) })
            }
        }
    }

    data class Tabular(val time : Time){
        companion object {
            fun parseTabular(tabular : Konsumer) : Tabular{
                tabular.checkCurrent("tabular")
                return Tabular(tabular.child("time") { Time.parseTime(
                        this,
                        this,
                        this,
                        this,
                        this,
                        this,
                        this
                ) })
            }
        }
    }

    data class Time(val timeFrom: String?,
                    val timeTo : String?,
                    val symbolName:String?,
                    val precipitation:String?,
                    val windDirection:String?,
                    val windSpeed:String?,
                    val temperature:String?,
                    val pressure:String?) {
        companion object {
            fun parseTime(time: Konsumer,
                          symbolName : Konsumer,
                          precipitation : Konsumer,
                          windDir : Konsumer,
                          windSpd : Konsumer,
                          temp : Konsumer,
                          pressure : Konsumer): Time {
                time.checkCurrent("time")
                symbolName.checkCurrent("symbol")
                precipitation.checkCurrent("precipitation")
                windDir.checkCurrent("windDirection")
                windSpd.checkCurrent("windSpeed")
                temp.checkCurrent("temperature")
                pressure.checkCurrent("pressure")
                return Time(time.attributes.getValue("from"),
                            time.attributes.getValue("to"),
                            symbolName.attributes.getValue("name"),
                            precipitation.attributes.getValue("value"),
                            windDir.attributes.getValue("deg"),
                            windSpd.attributes.getValue("mps"),
                            temp.attributes.getValue("value"),
                            pressure.attributes.getValue("value"))
            }
        }
    }

    fun parseXmlForecast(xml : String) : Forecast{
        return File(xml).konsumeXml().use { k -> k.child("forecast") { Forecast.parseForecast(this) } }
    }
}