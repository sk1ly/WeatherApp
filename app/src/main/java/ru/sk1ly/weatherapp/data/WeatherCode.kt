package ru.sk1ly.weatherapp.data

enum class WeatherCode(val code: Int, val description: String) {
    C0(0, "Clear sky"),
    C1(1, "Mainly clear"),
    C2(2, "Partly cloudy"),
    C3(3, "Overcast"),
    C45(45, "Fog"),
    C48(48, "Depositing rime fog"),
    C51(51, "Light drizzle"),
    C53(53, "Moderate drizzle"),
    C55(55, "Dense intensity drizzle"),
    C56(56, "Light freezing drizzle"),
    C57(57, "Dense intensity freezing drizzle"),
    C61(61, "Slight rain"),
    C63(63, "Moderate rain"),
    C65(65, "Heavy intensity rain"),
    C71(71, "Slight snow fall"),
    C73(73, "Moderate snow fall"),
    C75(75, "Heavy intensity snow fall"),
    C77(77, "Snow grains"),
    C80(80, "Slight rain showers"),
    C81(81, "Moderate rain showers"),
    C82(82, "Violent rain showers"),
    C85(85, "Snow showers slight"),
    C86(86, "Snow showers heavy"),
    C95(95, "Slight or moderate thunderstorm"),
    C96(96, "Thunderstorm with slight hail"),
    C99(99, "Thunderstorm with heavy hail");

    companion object {
        fun getDescription(code: Int) =
            values().filter { it.code == code }.firstOrNull()?.description?:"Unknown"
    }
}