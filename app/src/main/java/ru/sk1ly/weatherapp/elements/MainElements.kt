package ru.sk1ly.weatherapp.elements

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
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
import ru.sk1ly.weatherapp.data.City
import ru.sk1ly.weatherapp.data.Weather
import ru.sk1ly.weatherapp.data.WeatherCode
import ru.sk1ly.weatherapp.elements.autocomplete.AutoCompleteCitySample
import ru.sk1ly.weatherapp.ui.theme.BlueLight

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainCard(weather: MutableState<Weather>, context: Context) {

    Column(
        modifier = Modifier.padding(5.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = BlueLight,
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
                            text = weather.value.current.dateTime,
                            style = TextStyle(fontSize = 15.sp),
                            color = Color.White
                        )
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_sync),
                                tint = Color.White,
                                contentDescription = "Sync icon button"
                            )
                        }
                    }
                    Image(
                        // TODO Заменить на инфо, переместить к температуре
                        modifier = Modifier
                            .size(35.dp)
                            .padding(top = 5.dp, bottom = 5.dp, end = 8.dp),
                        painter = painterResource(id = R.drawable.sunny_cloudy),
                        contentDescription = "Weather icon",
                    )
                }
                Text(
                    text = weather.value.city.cityName,
                    style = TextStyle(fontSize = 24.sp),
                    color = Color.White
                )
                Text(
                    text = "${weather.value.current.temp}°C",
                    style = TextStyle(fontSize = 65.sp),
                    color = Color.White
                )
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
                        cities.add(City(
                            cityName = parts[0],
                            adminName = parts[1],
                            countryName = parts[2],
                            latitude = parts[3].toFloat(),
                            longitude = parts[4].toFloat()
                        ))
                    }
                    AutoCompleteCitySample(cities, weather, context)
                }
            }
        }
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
            backgroundColor = BlueLight,
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