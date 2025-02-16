package com.example.lab_1

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData

class WeatherViewModel: ViewModel() {
    private val weatherApiServices = Common.retrofitService
    private val _weatherData = MutableLiveData<WeatherForecast>()
    val weatherData: LiveData<WeatherForecast> get() = _weatherData
    private val _errorData = MutableLiveData<String?>()
    val errorData: LiveData<String?> get() = _errorData

    fun fetchWeather(cityName: String) {
        val appid = BuildConfig.OPEN_WEATHER_API_KEY

        viewModelScope.launch {
            weatherApiServices.getWeatherList(cityName, appid).enqueue(object : Callback<WeatherForecast> {
                override fun onResponse(call: Call<WeatherForecast>, response: Response<WeatherForecast>) {
                    if (response.isSuccessful) {
                        _weatherData.value = response.body()
                        _errorData.value = null
                    } else {
                        _errorData.value = "Город не найден"
                    }
                }

                override fun onFailure(call: Call<WeatherForecast>, t: Throwable) {
                    _errorData.value = "Ошибка соединения"
                }
            })
        }
    }

    fun clearErrors() {
        _errorData.value = null
    }
}