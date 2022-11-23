package ru.sk1ly.weatherapp.data

import ru.sk1ly.weatherapp.elements.autocomplete.AutoCompleteEntity
import java.util.*

data class City(
    val cityName : String = "Unknown",
    val adminName : String = "Unknown",
    val countryName: String = "Unknown",
    var latitude : Float = 0f,
    var longitude : Float = 0f,
) : AutoCompleteEntity {
    override fun filter(query: String): Boolean {
        return cityName.lowercase(Locale.getDefault())
            .startsWith(query.lowercase(Locale.getDefault()))
    }
}
