package com.example.billbuddy.newCurrencyExchange.domain.mapper

import android.graphics.Color
import com.example.billbuddy.newCurrencyExchange.domain.model.CurrencyEntity
import com.example.billbuddy.newCurrencyExchange.data.model.Rates

class CurrencyMapper {
    fun mapRatesToCurrencyEntities(rates: Rates?): List<CurrencyEntity> {
        return rates?.javaClass?.declaredFields?.map { field ->
            field.isAccessible = true
            val currencyCode = field.name
            val rate = field.get(rates) as? Double ?: 0.0
            CurrencyEntity(currencyCode, rate, 0.0, Color.YELLOW)
        } ?: emptyList()
    }
}