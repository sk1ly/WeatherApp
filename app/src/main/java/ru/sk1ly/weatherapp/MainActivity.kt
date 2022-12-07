package ru.sk1ly.weatherapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.google.gson.Gson
import ru.sk1ly.weatherapp.api.WeatherApiRequestor
import ru.sk1ly.weatherapp.data.City
import ru.sk1ly.weatherapp.data.Weather
import ru.sk1ly.weatherapp.elements.MainCard
import ru.sk1ly.weatherapp.elements.TabLayout
import ru.sk1ly.weatherapp.ui.theme.WeatherAppTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val mPrefs: SharedPreferences = getPreferences(MODE_PRIVATE)

        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                val weather: MutableState<Weather> = remember {
                    mutableStateOf(Weather())
                }
                getWeatherFromCacheOrGetDefault(mPrefs, weather)
                Image(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "Background",
                )
                Column {
                    MainCard(weather, mPrefs, LocalContext.current)
                    TabLayout(weather)
                }
            }
        }
    }

    private fun getWeatherFromCacheOrGetDefault(preferences: SharedPreferences, weather: MutableState<Weather>) {
        val json = preferences.getString("currentWeather", "")
        if (json.isNullOrBlank()) {
            WeatherApiRequestor.getWeather(City(), weather, preferences, this)
        } else {
            val currentWeather: Weather = Gson().fromJson(json, Weather::class.java)
            weather.value = currentWeather
        }
    }
}