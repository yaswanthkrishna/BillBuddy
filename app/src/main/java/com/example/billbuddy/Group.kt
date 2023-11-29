package com.example.billbuddy

data class Group(
    val id: String, // Unique identifier for the Group
    val name: String, // Group's name
    val amountOwed: Double, // Amount the group owes you
    val amountDue: Double // Amount you owe the group
) {
    // You can add additional properties or methods as needed
}
