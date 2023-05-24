package ru.sk1ly.weatherapp.elements.autocomplete

// See this: https://github.com/pauloaapereira/Medium_JetpackCompose_AutoCompleteSearchBar

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ru.sk1ly.weatherapp.api.WeatherApiRequestor
import ru.sk1ly.weatherapp.data.City
import ru.sk1ly.weatherapp.data.Weather
import ru.sk1ly.weatherapp.elements.TextSearchBar

@ExperimentalAnimationApi
@Composable
fun AutoCompleteCitySample(cities: List<City>, weather: MutableState<Weather>, preferences: SharedPreferences, context: Context) {

    AutoCompleteBox(
        items = cities,
        itemContent = { city ->
            CityAutoCompleteItem(city)
        }
    ) {
        var value by remember { mutableStateOf("") }
        val view = LocalView.current

        onItemSelected { city ->
            value = city.cityName
            filter(value)
            view.clearFocus()
            WeatherApiRequestor.getWeather(city, weather, preferences, context)
        }

        TextSearchBar(
            modifier = Modifier
                .testTag("AutoCompleteSearchBar")
                .padding(bottom = 8.dp),
            value = value,
            label = "Search city",
            onDoneActionClick = {
                view.clearFocus()
            },
            onClearClick = {
                value = ""
                filter(value)
                view.clearFocus()
            },
            onFocusChanged = { focusState ->
                isSearching = focusState.hasFocus
            },
            onValueChanged = { query ->
                if (query.length <= 10) {
                    value = query
                    filter(value)
                }
            }
        )
    }
}

@Composable
fun CityAutoCompleteItem(city: City) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "${city.cityName}, ${city.countryName}",
            color = Color.White
        )
    }
}