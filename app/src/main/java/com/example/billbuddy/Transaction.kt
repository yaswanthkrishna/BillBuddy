package com.example.billbuddy

data class Transaction(
    val description: String,
    val totalAmount: Double,
    val transactionDateTime: String
)