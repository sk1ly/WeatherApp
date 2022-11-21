package ru.sk1ly.weatherapp

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import ru.sk1ly.weatherapp.data.CurrentWeather
import ru.sk1ly.weatherapp.data.DailyWeather
import ru.sk1ly.weatherapp.data.HourlyWeather
import ru.sk1ly.weatherapp.data.Weather
import java.util.*

class WeatherApiRequestor {

    companion object {

        private const val API_URL = "https://api.open-meteo.com/v1/forecast"
        private const val CONST_PARAMS = "current_weather=true" +
                "&hourly=temperature_2m,weathercode" +
                "&daily=temperature_2m_max,temperature_2m_min,weathercode" +
                "&timezone=Europe/Moscow"

        fun getWeather(city: String, mutableState: MutableState<Weather>, context: Context) {
            val url =
                "$API_URL?latitude=55.75&longitude=37.62&$CONST_PARAMS" // TODO Убрать потом хардкод координат
            val queue = Volley.newRequestQueue(context)
            val request = StringRequest(
                Request.Method.GET,
                url,
                { response ->
                    mutableState.value = parseWeather(city, response)
                    Log.d("MyLog", "Response: $response")
                },
                { error ->
                    Log.d("MyLog", "VolleyError: $error")
                }
            )
            queue.add(request)
        }

        private fun parseWeather(city: String, response: String) : Weather {
            val responseJson = JSONObject(response)
            val weather = Weather(city = city)
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
            val currentHour = Calendar.getInstance(TimeZone.getTimeZone("Europe/Moscow")).get(Calendar.HOUR_OF_DAY)
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
    }
}