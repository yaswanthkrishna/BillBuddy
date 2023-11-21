package com.example.billbuddy_login

data class Friends(var friendId:String ,var transactionList: MutableList<Transactions> =mutableListOf<Transactions>() ){
    constructor():this(" ")

}