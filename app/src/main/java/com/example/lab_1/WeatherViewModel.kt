package com.example.lab_1

import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel: ViewModel() {
    private val weatherApiServices = Common.retrofitService
    private val _weatherData = MutableLiveData<WeatherForecast>()
    val weatherData: LiveData<WeatherForecast> get() = _weatherData

    fun fetchWeather(cityName: String, appid: String) {
        viewModelScope.launch {
            weatherApiServices.getWeatherList(cityName, appid).enqueue(object : Callback<WeatherForecast> {
                override fun onResponse(call: Call<WeatherForecast>, response: Response<WeatherForecast>) {
                    if (response.isSuccessful) {
                        _weatherData.value = response.body()
                    }
                }

                override fun onFailure(call: Call<WeatherForecast>, t: Throwable) {  }
            })
        }
    }
}