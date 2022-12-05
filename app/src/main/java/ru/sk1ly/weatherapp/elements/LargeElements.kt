package ru.sk1ly.weatherapp.elements

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import ru.sk1ly.weatherapp.R
import ru.sk1ly.weatherapp.api.WeatherApiRequestor
import ru.sk1ly.weatherapp.data.City
import ru.sk1ly.weatherapp.data.Weather
import ru.sk1ly.weatherapp.data.WeatherCode
import ru.sk1ly.weatherapp.elements.autocomplete.AutoCompleteCitySample
import ru.sk1ly.weatherapp.ui.theme.DarkDeepBlue

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainCard(weather: MutableState<Weather>, context: Context) {

    Column(
        modifier = Modifier.padding(5.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = DarkDeepBlue,
            shape = RoundedCornerShape(10.dp),
            elevation = 0.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Last update on " + weather.value.requestedDateTime.split(" ")[1],
                            style = TextStyle(fontSize = 15.sp),
                            color = Color.White
                        )
                        SyncButtonAndTime(weather, context)
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_info),
                            tint = Color.White,
                            contentDescription = "Info icon button"
                        )
                    }
                }
                Text(
                    text = weather.value.city.cityName,
                    style = TextStyle(fontSize = 24.sp),
                    color = Color.White
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${weather.value.current.temp.toFloat().toInt()}°C",
                        style = TextStyle(fontSize = 65.sp),
                        color = Color.White
                    )
                    Image(
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .size(65.dp, 65.dp),
                        painter = painterResource(id = WeatherCode.getDrawable(weather.value.current.weatherCode)),
                        contentDescription = "Current weather icon"
                    )
                }
                Text(
                    text = WeatherCode.getDescription(weather.value.current.weatherCode),
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.White
                )
                Text(
                    modifier = Modifier.padding(top = 5.dp),
                    text =
                    if (weather.value.daily.isNotEmpty()) {
                        "${weather.value.daily[0].maxTemp}/${weather.value.daily[0].minTemp}°C"
                    } else {
                        "0/0°C"
                    },
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.White
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, top = 20.dp)
                            .size(35.dp, 35.dp),
                        painter = painterResource(R.drawable.ic_search),
                        tint = Color.White,
                        contentDescription = "Search icon button"
                    )
                    val cities = mutableListOf<City>()
                    stringArrayResource(id = R.array.cities).forEach {
                        val parts = it.split(";")
                        cities.add(
                            City(
                                cityName = parts[0],
                                adminName = parts[1],
                                countryName = parts[2],
                                latitude = parts[3].toFloat(),
                                longitude = parts[4].toFloat()
                            )
                        )
                    }
                    AutoCompleteCitySample(cities, weather, context)
                }
            }
        }
    }
}

@Composable
fun SyncButtonAndTime(weather: MutableState<Weather>, context: Context) {

    var isRotated by remember {
        mutableStateOf(false)
    }

    val angle: Float by animateFloatAsState(
        targetValue = if (isRotated) 360F else 0F,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing,
        )
    )

    IconButton(onClick = {
        isRotated = !isRotated
        WeatherApiRequestor.getWeather(
            weather.value.city,
            weather,
            context
        )
    }) {
        Icon(
            modifier = Modifier.rotate(angle),
            painter = painterResource(R.drawable.ic_sync),
            tint = Color.White,
            contentDescription = "Sync icon button"
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(weather: MutableState<Weather>) {
    val tabList = listOf("HOURS", "DAYS")
    val pagerState = rememberPagerState()
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
            indicator = { pos ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, pos)
                )
            },
            backgroundColor = DarkDeepBlue,
            contentColor = Color.White
        ) {
            tabList.forEachIndexed { index, text ->
                Tab(
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = text)
                    })
            }
        }
        HorizontalPager(
            count = tabList.size,
            state = pagerState,
            modifier = Modifier.weight(1.0f)
        ) { index ->
            when (index) {
                0 -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(weather.value.hourly) { item ->
                            ListItem(hourlyWeather = item)
                        }
                    }
                }
                1 -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(weather.value.daily) { item ->
                            ListItem(dailyWeather = item)
                        }
                    }
                }
            }
        }
    }
}