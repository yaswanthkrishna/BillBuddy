package com.example.billbuddy.newCurrencyExchange.presentation.main

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billbuddy.newCurrencyExchange.data.remote.ApiClient
import com.example.billbuddy.newCurrencyExchange.data.repository.CurrencyRepository
import com.example.billbuddy.newCurrencyExchange.data.utils.MY_GREEN
import com.example.billbuddy.newCurrencyExchange.data.utils.MY_RED
import com.example.billbuddy.newCurrencyExchange.domain.mapper.CurrencyMapper
import com.example.billbuddy.newCurrencyExchange.domain.model.CurrencyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainViewModel : ViewModel() {
    private val _currencyData = MutableLiveData<List<CurrencyEntity>>()
    val currencyData: LiveData<List<CurrencyEntity>> = _currencyData

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val currencyRepository = CurrencyRepository(ApiClient.provideApi())
    private val currencyMapper = CurrencyMapper()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val currentDate = dateFormat.format(Date())

    fun fetchCurrencies(apiKey: String, baseCurrency: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _loading.postValue(true)

                val currentRatesResponse = currencyRepository.getCurrencies(currentDate, apiKey, baseCurrency)
                if (currentRatesResponse.isSuccessful) {
                    val currentRates = currentRatesResponse.body()?.rates

                    val calendar = Calendar.getInstance()
                    calendar.add(Calendar.DATE, -1)
                    val previousDate = dateFormat.format(calendar.time)

                    val previousRatesResponse = currencyRepository.getCurrencies(previousDate, apiKey, baseCurrency)
                    val previousRates = previousRatesResponse.body()?.rates

                    if (currentRates != null && previousRates != null) {
                        val currencyList = currencyMapper.mapRatesToCurrencyEntities(currentRates)
                        val previousList = currencyMapper.mapRatesToCurrencyEntities(previousRates)
                        currencyList.forEach { currency ->
                            val previousRate = previousList.find { it.name == currency.name }?.currentRate ?: 0.0
                            val rateChange = currency.currentRate - previousRate
                            currency.rateChange = rateChange

                            currency.color = if (rateChange >= 0) Color.parseColor(MY_GREEN)
                            else Color.parseColor(MY_RED)

                        }
                        _currencyData.postValue(currencyList)
                    } else {
                        _error.postValue("No currency data available")
                    }
                } else {
                    _error.postValue("Response is not successful")
                }
            } catch (e: Exception) {
                _error.postValue("An error occurred: ${e.message}")
            } finally {
                _loading.postValue(false)
            }
        }
    }
}

