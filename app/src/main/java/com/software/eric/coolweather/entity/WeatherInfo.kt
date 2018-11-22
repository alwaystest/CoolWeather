package com.software.eric.coolweather.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by eric on 2018/11/20
 */
data class WeatherInfo(
        @SerializedName("HeWeather6")
        val heWeather: List<HeWeather>
)

data class HeWeather(
        @SerializedName("basic")
        val basic: Basic,
        @SerializedName("daily_forecast")
        val dailyForecast: List<DailyForecast>,
        @SerializedName("status")
        val status: String,
        @SerializedName("update")
        val update: Update,
        @SerializedName("now")
        val now: Now
)

data class Now(
    @SerializedName("cond_code")
    val condCode: String,
    @SerializedName("cond_txt")
    val condTxt: String,
    @SerializedName("fl")
    val fl: String,
    @SerializedName("hum")
    val hum: String,
    @SerializedName("pcpn")
    val pcpn: String,
    @SerializedName("pres")
    val pres: String,
    @SerializedName("tmp")
    val tmp: String,
    @SerializedName("vis")
    val vis: String,
    @SerializedName("wind_deg")
    val windDeg: String,
    @SerializedName("wind_dir")
    val windDir: String,
    @SerializedName("wind_sc")
    val windSc: String,
    @SerializedName("wind_spd")
    val windSpd: String
)

data class Basic(
        @SerializedName("admin_area")
        val adminArea: String,
        @SerializedName("cid")
        val cid: String,
        @SerializedName("cnty")
        val cnty: String,
        @SerializedName("lat")
        val lat: String,
        @SerializedName("location")
        val location: String,
        @SerializedName("lon")
        val lon: String,
        @SerializedName("parent_city")
        val parentCity: String,
        @SerializedName("tz")
        val tz: String
)

data class Update(
        @SerializedName("loc")
        val loc: String,
        @SerializedName("utc")
        val utc: String
)

data class DailyForecast(
        @SerializedName("cond_code_d")
        val condCodeD: String,
        @SerializedName("cond_code_n")
        val condCodeN: String,
        @SerializedName("cond_txt_d")
        val condTxtD: String,
        @SerializedName("cond_txt_n")
        val condTxtN: String,
        @SerializedName("date")
        val date: String,
        @SerializedName("hum")
        val hum: String,
        @SerializedName("pcpn")
        val pcpn: String,
        @SerializedName("pop")
        val pop: String,
        @SerializedName("pres")
        val pres: String,
        @SerializedName("tmp_max")
        val tmpMax: String,
        @SerializedName("tmp_min")
        val tmpMin: String,
        @SerializedName("uv_index")
        val uvIndex: String,
        @SerializedName("vis")
        val vis: String,
        @SerializedName("wind_deg")
        val windDeg: String,
        @SerializedName("wind_dir")
        val windDir: String,
        @SerializedName("wind_sc")
        val windSc: String,
        @SerializedName("wind_spd")
        val windSpd: String
)
