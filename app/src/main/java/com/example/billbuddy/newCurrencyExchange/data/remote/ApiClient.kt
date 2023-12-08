package com.example.billbuddy.newCurrencyExchange.data.remote

import com.example.billbuddy.newCurrencyExchange.data.utils.BASE_URL

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    fun provideApi() : ApiService {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}