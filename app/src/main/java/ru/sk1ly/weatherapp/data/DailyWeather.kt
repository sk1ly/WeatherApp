package ru.sk1ly.weatherapp.data

data class DailyWeather(
    val date: String,
    val maxTemp: String,
    val minTemp: String,
    val weatherCode: String
)