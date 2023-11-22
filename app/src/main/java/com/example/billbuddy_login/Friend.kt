package com.example.billbuddy_login

data class Friend(
    val id: String, // Unique identifier for the friend
    val name: String, // Friend's name
    val amountOwed: Double, // Amount the friend owes you
    val amountDue: Double // Amount you owe the friend
) {
    // You can add additional properties or methods as needed
}
