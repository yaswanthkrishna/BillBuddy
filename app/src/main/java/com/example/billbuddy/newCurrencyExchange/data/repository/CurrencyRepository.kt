package com.example.billbuddy.newCurrencyExchange.data.repository

import com.example.billbuddy.newCurrencyExchange.data.model.CurrencyResponse
import com.example.billbuddy.newCurrencyExchange.data.remote.ApiService
import retrofit2.Response

class CurrencyRepository(private val apiService: ApiService) {
    suspend fun getCurrencies(date: String, apiKey: String, baseCurrency: String): Response<CurrencyResponse> {
        return apiService.getCurrenciesWithDate(date, apiKey, baseCurrency)
    }
}
