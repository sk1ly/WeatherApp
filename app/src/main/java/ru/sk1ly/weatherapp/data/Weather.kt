package ru.sk1ly.weatherapp.data

data class Weather(
    var city: City = City(),
    var requestedDateTime: String = "01.01.2022 00:00",
    var current: CurrentWeather = CurrentWeather(),
    var hourly: List<HourlyWeather> = mutableListOf(),
    var daily: List<DailyWeather> = mutableListOf()
)
