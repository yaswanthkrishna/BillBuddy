package com.example.billbuddy

data class Friend(
    val id: Long,
    val userId: Long,
    val friendUserId: Long,
    val owe: Double,
    val owes: Double,
    val totalDue: Double,
    val name: String
)