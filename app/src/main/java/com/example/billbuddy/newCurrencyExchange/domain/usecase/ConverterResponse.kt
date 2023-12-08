package com.example.billbuddy.newCurrencyExchange.domain.usecase

import com.example.billbuddy.newCurrencyExchange.data.model.Rates
import com.google.gson.annotations.SerializedName

data class ConverterResponse(
    @SerializedName("base")
    val base: String,
    @SerializedName("rates")
    val rates: Rates?
)
