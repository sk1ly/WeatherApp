package ru.sk1ly.weatherapp.data

import ru.sk1ly.weatherapp.R

enum class WeatherCode(val code: Int, val description: String, val drawableId: Int) {
    C0(0, "Clear sky", R.drawable.sunny),
    C1(1, "Mainly clear", R.drawable.sunny_cloudy),
    C2(2, "Partly cloudy", R.drawable.sunny_cloudy),
    C3(3, "Overcast", R.drawable.cloudy),
    C45(45, "Fog", R.drawable.fog),
    C48(48, "Depositing rime fog", R.drawable.fog),
    C51(51, "Light drizzle", R.drawable.sunny_rainy),
    C53(53, "Moderate drizzle", R.drawable.sunny_rainy),
    C55(55, "Dense intensity drizzle", R.drawable.sunny_rainy),
    C56(56, "Light freezing drizzle", R.drawable.snow_rainy),
    C57(57, "Dense intensity freezing drizzle", R.drawable.snow_rainy),
    C61(61, "Slight rain", R.drawable.rainy),
    C63(63, "Moderate rain", R.drawable.rainy),
    C65(65, "Heavy intensity rain", R.drawable.rainy),
    C71(71, "Slight snow fall", R.drawable.snow),
    C73(73, "Moderate snow fall", R.drawable.snow),
    C75(75, "Heavy intensity snow fall", R.drawable.snow),
    C77(77, "Snow grains", R.drawable.snow),
    C80(80, "Slight rain showers", R.drawable.rainy),
    C81(81, "Moderate rain showers", R.drawable.rainy),
    C82(82, "Violent rain showers", R.drawable.rainy),
    C85(85, "Snow showers slight", R.drawable.snow),
    C86(86, "Snow showers heavy", R.drawable.snow),
    C95(95, "Slight or moderate thunderstorm", R.drawable.lightening),
    C96(96, "Thunderstorm with slight hail", R.drawable.lightening),
    C99(99, "Thunderstorm with heavy hail", R.drawable.lightening);

    companion object {
        fun getDescription(code: Int) = values().filter { it.code == code }.firstOrNull()?.description?:"Unknown"
        fun getDrawable(code: Int) = values().filter { it.code == code }.firstOrNull()?.drawableId?:R.drawable.unknown
    }
}