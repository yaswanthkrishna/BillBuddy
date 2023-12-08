package com.example.billbuddy.newCurrencyExchange.data.remote

import com.example.billbuddy.newCurrencyExchange.data.model.CurrencyResponse
import com.example.billbuddy.newCurrencyExchange.domain.usecase.ConverterResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("{date}")
    suspend fun getCurrenciesWithDate(
        @Path("date") date: String,
        @Query("access_key") apiKey: String,
        @Query("base") baseCurrency: String
    ): Response<CurrencyResponse>

    @GET("latest")
    suspend fun getCurrencyRates(
        @Query("access_key") apiKey: String,
        @Query("base") baseCurrency: String,
        @Query("symbols") convertedToCurrency: String
    ): Response<ConverterResponse>


}