package com.example.billbuddy

data class Group(
    val id: Long,
    val userId: Long,
    val groupUserId: Int,
    val owe: Double, // This is the amount the user owes to the group
    val owes: Double, // This is the amount the group owes to the user
    val name: String
)
