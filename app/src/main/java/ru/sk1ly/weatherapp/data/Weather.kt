package ru.sk1ly.weatherapp.data

data class Weather(
    var city: City = City(),
    var requestedDateTime: Long = 0,
    var current: CurrentWeather = CurrentWeather(),
    var hourly: List<HourlyWeather> = mutableListOf(),
    var daily: List<DailyWeather> = mutableListOf()
)
