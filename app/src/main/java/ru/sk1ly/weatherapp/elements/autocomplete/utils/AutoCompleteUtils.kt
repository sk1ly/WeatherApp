package ru.sk1ly.weatherapp.elements.autocomplete.utils

// See this: https://github.com/pauloaapereira/Medium_JetpackCompose_AutoCompleteSearchBar

import ru.sk1ly.weatherapp.elements.autocomplete.ValueAutoCompleteEntity

typealias CustomFilter<T> = (T, String) -> Boolean

fun <T> List<T>.asAutoCompleteEntities(filter: CustomFilter<T>): List<ValueAutoCompleteEntity<T>> {
    return map {
        object : ValueAutoCompleteEntity<T> {
            override val value: T = it

            override fun filter(query: String): Boolean {
                return filter(value, query)
            }
        }
    }
}

const val AutoCompleteSearchBarTag = "AutoCompleteSearchBar"