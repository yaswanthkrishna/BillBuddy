package com.example.billbuddy.yaswanth

data class MemberDetail(
    val userId: Long,
    val name: String,
    val amountOwe: Double  // Make sure this name matches the alias in your SQL query
)

