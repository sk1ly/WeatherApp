package ru.sk1ly.weatherapp.data

enum class WeatherCode(val code: Int, val description: String) {
    C0(0, "Clear sky"),
    C1(1, "Mainly clear"),
    C2(2, "Partly cloudy"),
    C3(3, "Overcast"),
    C45(45, "Fog"),
    C48(48, "Depositing rime fog"),
    C51(51, "Drizzle: light"),
    C53(53, "Drizzle: moderate"),
    C55(55, "Drizzle: dense intensity"),
    C56(56, "Freezing Drizzle: light"),
    C57(57, "Freezing Drizzle: dense intensity"),
    C61(61, "Rain: slight"),
    C63(63, "Rain: moderate"),
    C65(65, "Rain: heavy intensity"),
    C71(71, "Snow fall: slight"),
    C73(73, "Snow fall: moderate"),
    C75(75, "Snow fall: heavy intensity"),
    C80(80, "Rain showers: slight"),
    C81(81, "Rain showers: moderate"),
    C82(82, "Rain showers: violent"),
    C85(85, "Snow showers slight"),
    C86(86, "Snow showers heavy"),
    C95(95, "Thunderstorm: Slight or moderate"),
    C96(96, "Thunderstorm with slight hail"),
    C99(99, "Thunderstorm with heavy hail");

    companion object {
        fun getDescription(code: Int) = values().first { it.code == code }.description
    }
}