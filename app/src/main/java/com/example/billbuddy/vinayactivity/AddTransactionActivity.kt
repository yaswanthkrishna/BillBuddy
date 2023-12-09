package com.example.billbuddy.vinayactivity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.databinding.ActivityAddTransactionBinding
import com.example.billbuddy.vinay.database.groups.GroupEntity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.database.transactions.TransactionEntity
import com.example.billbuddy.vinay.viewmodels.FriendTransactionViewModel
import com.example.billbuddy.vinay.viewmodels.FriendTransactionViewModelFactory
import com.example.billbuddy.vinay.viewmodels.GroupTransactionViewModel
import com.example.billbuddy.vinay.viewmodels.GroupTransactionViewModelFactory
import com.example.billbuddy.vinay.viewmodels.GroupViewModel
import com.example.billbuddy.vinay.viewmodels.GroupViewModelFactory
import com.example.billbuddy.vinay.viewmodels.UserViewModel
import com.example.billbuddy.vinay.viewmodels.UserViewModelFactory
import com.example.billbuddy.vinay.views.SplitwiseApplication
//import kotlinx.android.synthetic.main.activity_add_transaction.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var groupViewModel: GroupViewModel
    private lateinit var friendTransactionViewModel: FriendTransactionViewModel
    private lateinit var transactionViewModel: GroupTransactionViewModel
    private val preferenceHelper by lazy { PreferenceHelper(this) }
    private lateinit var groupEntity: GroupEntity
    private lateinit var binding: ActivityAddTransactionBinding

    override fun onResume() {
        super.onResume()
        getGroupEntity()
    }

    private fun getGroupEntity() {
        groupViewModel.getGroupList().observe(this, Observer {
            for (i in it) {
                if (i.id == intent.getIntExtra("id", 0)) {
                    groupEntity = i
                    break
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createDatabase()
        binding.etNameEmailPhone.text = intent.getStringExtra("name")
        binding.tvBackArrow.setOnClickListener {
            finish()
        }
        binding.addAllUsers.setOnClickListener {
            var amt = 0
            if (intent.getIntExtra("size", 1) != 0) {
                amt = binding.tvAmount.text.toString().toInt() / intent.getIntExtra("size", 1)
            }
            amt = (binding.tvAmount.text.toString().toInt() - amt)
            val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
            /*val entity = TransactionEntity(
                intent.getIntExtra("id", 0),
                preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID),
                dtf.format(LocalDateTime.now()),
                amt,
                binding.tvAmount.text.toString().toInt(),
                "INR",
                binding.tvDescription.text.toString()
            )
            transactionViewModel.addTransaction(entity)*/
            val x = groupEntity.amt
            groupEntity.amt = x + amt
            groupViewModel.updateGroup(groupEntity)
            finish()
        }
    }

    private fun createDatabase() {
        val appClass = application as SplitwiseApplication
        val userRepository = appClass.userRepository
        //val groupRepository = appClass.groupRepository
        val transactionRepository = appClass.transactionRepository
        val friendRepository = appClass.friendsRepository

        val userViewModelFactory = UserViewModelFactory(userRepository)
        //val groupViewModelFactory = GroupViewModelFactory(groupRepository)
        //val transactionViewModelFactory = GroupTransactionViewModelFactory(transactionRepository)
        val friendViewModelFactory = FriendTransactionViewModelFactory(friendRepository)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        //groupViewModel = ViewModelProvider(this, groupViewModelFactory)
            //.get(GroupViewModel::class.java)

        friendTransactionViewModel = ViewModelProvider(this, friendViewModelFactory)
            .get(FriendTransactionViewModel::class.java)

        //transactionViewModel = ViewModelProvider(this, transactionViewModelFactory)
            //.get(GroupTransactionViewModel::class.java)
    }
}
