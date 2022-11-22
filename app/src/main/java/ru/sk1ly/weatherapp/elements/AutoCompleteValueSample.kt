package ru.sk1ly.weatherapp.elements

// See this: https://github.com/pauloaapereira/Medium_JetpackCompose_AutoCompleteSearchBar

import android.content.Context
import android.opengl.Visibility
import android.view.View
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import ru.sk1ly.weatherapp.WeatherApiRequestor
import ru.sk1ly.weatherapp.data.Weather
import ru.sk1ly.weatherapp.elements.autocomplete.AutoCompleteBox
import ru.sk1ly.weatherapp.elements.autocomplete.utils.AutoCompleteSearchBarTag
import ru.sk1ly.weatherapp.elements.autocomplete.utils.asAutoCompleteEntities
import ru.sk1ly.weatherapp.elements.TextSearchBar
import java.util.Locale

@ExperimentalAnimationApi
@Composable
fun AutoCompleteValueSample(items: List<String>, weather: MutableState<Weather>, context: Context) {

    val autoCompleteEntities = items.asAutoCompleteEntities(
        filter = { item, query ->
            item.lowercase(Locale.getDefault())
                .startsWith(query.lowercase(Locale.getDefault()))
        }
    )

    AutoCompleteBox(
        items = autoCompleteEntities,
        itemContent = { item ->
            ValueAutoCompleteItem(item.value)
        }
    ) {
        var value by remember { mutableStateOf("") }
        val view = LocalView.current

        onItemSelected { item ->
            value = item.value
            filter(value)
            view.clearFocus()
            WeatherApiRequestor.getWeather(item.value, weather, context)
        }

        TextSearchBar(
            modifier = Modifier.testTag(AutoCompleteSearchBarTag).padding(bottom = 8.dp),
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
                value = query
                filter(value)
            }
        )
    }
}

@Composable
fun ValueAutoCompleteItem(item: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = item, style = MaterialTheme.typography.subtitle2, color = Color.White)
    }
}