package com.example.billbuddy.newCurrencyExchange.presentation.converter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billbuddy.newCurrencyExchange.domain.usecase.CurrencyConverter
import kotlinx.coroutines.launch

class ConverterViewModel : ViewModel() {

    private val _convertedToCurrency = MutableLiveData("USD")

    val _conversionRate = MutableLiveData<Double?>()

    private val _convertedValue = MutableLiveData<String>()
    val convertedValue: LiveData<String> get() = _convertedValue

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val currencyConverter = CurrencyConverter()

    fun setConvertedToCurrency(currency: String,amount:String) {
        _convertedToCurrency.value = currency
        fetchConversionRate(amount)
    }

    fun fetchConversionRate(amount:String) {
        val convertedToCurrency = _convertedToCurrency.value

        if (!convertedToCurrency.isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    val conversionRate = currencyConverter.convertCurrency(convertedToCurrency)
                    if (conversionRate != null) {
                        _conversionRate.value = conversionRate
                        Log.d("fetchConversionRate_conversionRate",_conversionRate.value.toString())
                        // Call convertValue only after conversionRate is set
                        convertValue(amount)  // Pass a default amount for testing
                    } else {
                        _errorMessage.value = "Currency conversion failed."
                    }
                } catch (e: Exception) {
                    _errorMessage.value = "Error: ${e.message}"
                }
            }
        }
    }

    fun convertValue(amountStr: String?) {
        if (!amountStr.isNullOrEmpty()) {
            val amount = amountStr.toDouble()
            Log.d("conversionRateamount","$amount")
            Log.d("conversionRate_conversionRate",_conversionRate.value.toString())
            val conversion = amount * (_conversionRate.value ?: 0.0)
            Log.d("conversionRateconversion","$conversion")
            _convertedValue.value = conversion.toString()
        } else {
            _errorMessage.value = "Please enter an amount"
        }
    }
}