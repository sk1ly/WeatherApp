package ru.sk1ly.weatherapp.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONObject
import ru.sk1ly.weatherapp.data.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherApiRequestor {

    companion object {

        private const val WEATHER_API_URL = "https://api.open-meteo.com/v1/forecast"
        private const val WEATHER_API_CONST_PARAMS = "current_weather=true" +
                "&hourly=temperature_2m,weathercode" +
                "&daily=temperature_2m_max,temperature_2m_min,weathercode" +
                "&timezone="

        fun getWeather(city: City, mutableState: MutableState<Weather>, preferences: SharedPreferences, context: Context) {
            val url =
                "${WEATHER_API_URL}?latitude=${city.latitude}&longitude=${city.longitude}&${WEATHER_API_CONST_PARAMS}" + TimeZone.getDefault().id
            val queue = Volley.newRequestQueue(context)
            val request = StringRequest(
                Request.Method.GET,
                url,
                { response ->
                    Log.d("MyLog", "WeatherApiResponse: $response")
                    mutableState.value = parseWeather(city, response)
                    putWeatherToCache(mutableState.value, preferences)
                },
                { error ->
                    Log.d("MyLog", "VolleyError: $error")
                }
            )
            queue.add(request)
        }

        private fun parseWeather(city: City, response: String): Weather {
            val responseJson = JSONObject(response)
            val weather = Weather(city = city)
            weather.requestedDateTime = Calendar.getInstance().timeInMillis
            weather.current = parseCurrentWeather(responseJson)
            weather.hourly = parseHourlyWeather(responseJson)
            weather.daily = parseDailyWeather(responseJson)
            return weather
        }

        private fun parseCurrentWeather(responseJson: JSONObject): CurrentWeather {
            val json = responseJson.getJSONObject("current_weather")
            return CurrentWeather(
                dateTime = json.getString("time"),
                temp = json.getString("temperature"),
                weatherCode = json.getInt("weathercode")
            )
        }

        private fun parseHourlyWeather(responseJson: JSONObject): List<HourlyWeather> {
            val hourlyJson = responseJson.getJSONObject("hourly")
            val timeArray = hourlyJson.getJSONArray("time")
            val tempArray = hourlyJson.getJSONArray("temperature_2m")
            val weatherCodeArray = hourlyJson.getJSONArray("weathercode")
            val hourlyWeather = mutableListOf<HourlyWeather>()
            val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            for (i in currentHour until currentHour + 24) {
                hourlyWeather.add(
                    HourlyWeather(
                        time = timeArray.getString(i).split("T")[1],
                        temp = tempArray.getString(i),
                        weatherCode = weatherCodeArray.getInt(i)
                    )
                )
            }
            return hourlyWeather
        }

        private fun parseDailyWeather(responseJson: JSONObject): List<DailyWeather> {
            val dailyJson = responseJson.getJSONObject("daily")
            val dateArray = dailyJson.getJSONArray("time")
            val tempMaxArray = dailyJson.getJSONArray("temperature_2m_max")
            val tempMinArray = dailyJson.getJSONArray("temperature_2m_min")
            val weatherCodeArray = dailyJson.getJSONArray("weathercode")
            val dailyWeather = mutableListOf<DailyWeather>()
            for (i in 0 until 7) {
                dailyWeather.add(
                    DailyWeather(
                        date = dateArray.getString(i),
                        maxTemp = tempMaxArray.getInt(i),
                        minTemp = tempMinArray.getInt(i),
                        weatherCode = weatherCodeArray.getInt(i)
                    )
                )
            }
            return dailyWeather
        }

        private fun putWeatherToCache(weather: Weather, preferences: SharedPreferences) {
            val prefsEditor = preferences.edit()
            val gson = Gson()
            val json = gson.toJson(weather)
            prefsEditor.putString("currentWeather", json)
            prefsEditor.apply()
        }
    }
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}