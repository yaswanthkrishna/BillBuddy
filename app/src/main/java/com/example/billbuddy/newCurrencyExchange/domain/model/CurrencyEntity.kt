package com.example.billbuddy.newCurrencyExchange.domain.model

data class CurrencyEntity(
    val name: String,
    val currentRate: Double,
    var rateChange: Double,
    var color: Int
)
