package com.example.lab_1

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab_1.ui.theme.Lab_1Theme
import com.skydoves.landscapist.glide.GlideImage

@Composable
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun WeatherView(viewModel: WeatherViewModel = viewModel()) {
    var city by remember { mutableStateOf("Кемерово") }
    val weather by viewModel.weatherData.observeAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Погода") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Введите город") },
                    modifier = Modifier
                        .weight(2f)
                        .padding(end = 8.dp)
                )

                Button(
                    onClick = { viewModel.fetchWeather(city) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Найти")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                weather?.let { weather ->
                    itemsIndexed(weather.list) {index, item ->
                        WeatherItem(weather = weather.list[index])
                    }
                } ?: item {
                    Text("Данных пока нет", modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun WeatherItem(weather: WeatherEntry) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = String.format("Температура: %.0f °C", weather.main.temp - 273.15), fontSize = 20.sp)
            Text(text = "Давление: ${weather.main.pressure} Па", fontSize = 16.sp)
            GlideImage(
                imageModel = {"https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png" },
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun WeatherViewPreview() {
    Lab_1Theme {
        WeatherView()
    }
}