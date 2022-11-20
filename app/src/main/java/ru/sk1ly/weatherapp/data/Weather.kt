package ru.sk1ly.weatherapp.data

data class Weather(
    var city: String = "Unknown",
    var current: CurrentWeather = CurrentWeather(),
    var hourly: List<HourlyWeather> = mutableListOf(),
    var daily: List<DailyWeather> = mutableListOf()
)
