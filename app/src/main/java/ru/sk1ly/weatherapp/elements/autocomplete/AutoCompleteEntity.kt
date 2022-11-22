package ru.sk1ly.weatherapp.elements.autocomplete

// See this: https://github.com/pauloaapereira/Medium_JetpackCompose_AutoCompleteSearchBar

import androidx.compose.runtime.Stable

@Stable
interface AutoCompleteEntity {
    fun filter(query: String): Boolean
}

@Stable
interface ValueAutoCompleteEntity<T> : AutoCompleteEntity {
    val value: T
}