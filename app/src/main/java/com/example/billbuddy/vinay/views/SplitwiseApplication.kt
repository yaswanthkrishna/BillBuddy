package com.example.billbuddy.vinay.views

import android.app.Application
import com.example.billbuddy.vinay.database.SplitwiseDatabase
import com.example.billbuddy.vinay.repositories.FriendRepository
import com.example.billbuddy.vinay.repositories.FriendTransactionRepository
import com.example.billbuddy.vinay.repositories.GroupListRepository
import com.example.billbuddy.vinay.repositories.GroupMemberRepository
import com.example.billbuddy.vinay.repositories.UserRepository
import com.example.billbuddy.vinay.repositories.GroupTransactionMemberRepository
import com.example.billbuddy.vinay.repositories.GroupTransactionRepository
import com.example.billbuddy.vinay.repositories.NonGroupTransactionMemberRepository
import com.example.billbuddy.vinay.repositories.TransactionRepository
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction

class SplitwiseApplication : Application() {

    companion object {
        val PREF_IS_USER_LOGIN = "PREF_LOGIN_BOOLEAN_KEY"
        val PREF_USER_ID = "PREF_LOGIN_USER_ID"
    }

    val userDAO by lazy {
        val roomDatabase = SplitwiseDatabase.getDatabase(this)
        roomDatabase.getMyUserEntries()
    }
    val friendTransactionDAO by lazy {
        val roomDatabase = SplitwiseDatabase.getDatabase(this)
        roomDatabase.getMyFriendTransactionEntries()
    }
    val transactionDAO by lazy {
        val roomDatabase = SplitwiseDatabase.getDatabase(this)
        roomDatabase.getMyTransactionEntries()
    }
    val groupTransactionMemberDAO by lazy {
        val roomDatabase = SplitwiseDatabase.getDatabase(this)
        roomDatabase.getMyGroupTransactionMemberEntries()
    }
    val nonGroupTransactionMemberDAO by lazy {
        val roomDatabase = SplitwiseDatabase.getDatabase(this)
        roomDatabase.getMyNonGroupTransactionMemberEntries()
    }
    val friendDAO by lazy{
        val roomDatabase = SplitwiseDatabase.getDatabase(this)
        roomDatabase.getMyFriendEntries()
    }
    val groupListDAO by lazy{
        val roomDatabase = SplitwiseDatabase.getDatabase(this)
        roomDatabase.getMyGroupListEntries()
    }
    val groupMemberDAO by lazy {
        val roomDatabase = SplitwiseDatabase.getDatabase(this)
        roomDatabase.getMyGroupMemberEntries()
    }
    val groupTransactionDAO by lazy{
        val roomDatabase = SplitwiseDatabase.getDatabase(this)
        roomDatabase.getMyGroupTransactionEntries()
    }

    val userRepository by lazy {
        UserRepository(userDAO)
    }
    val transactionRepository by lazy {
        TransactionRepository(transactionDAO)
    }
    val friendsRepository by lazy {
        FriendTransactionRepository(friendTransactionDAO)
    }
    val groupTransactionMemberRepository by lazy{
        GroupTransactionMemberRepository(groupTransactionMemberDAO)
    }
    val nongroupTransactionMemberRepository by lazy{
        NonGroupTransactionMemberRepository(nonGroupTransactionMemberDAO)
    }
    val friendRepository by lazy {
        FriendRepository(friendDAO)
    }
    val groupListRepository by lazy {
        GroupListRepository(groupListDAO)
    }
    val groupMemberRepository by lazy {
        GroupMemberRepository(groupMemberDAO)
    }
    val groupTransactionRepository by lazy {
        GroupTransactionRepository(groupTransactionDAO)
    }

    var clientID = "AR8RFp4OHpvKY0cQ9of4f3P5LI0z7FtjJeHSCUCNZTx4JA3o3o8uoUZs6gKEmsVEQ2Ap6qmsaymosss7"
    var returnUrl = "com.example.billbuddy://paypalpay"
    override fun onCreate() {
        super.onCreate()
        PayPalCheckout.setConfig(
            CheckoutConfig(
                application = this,
                clientId = clientID,
                environment = Environment.SANDBOX,
                returnUrl = returnUrl,
                currencyCode = CurrencyCode.USD,
                userAction = UserAction.PAY_NOW,
                settingsConfig = SettingsConfig(
                    loggingEnabled = true
                )
            )
        )
    }
}