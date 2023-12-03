package com.example.billbuddy.vinayactivity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.billbuddy.databinding.ActivityCreateGroupBinding
import com.example.billbuddy.vinay.database.groups.GroupEntity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.viewmodels.FriendTransactionViewModel
import com.example.billbuddy.vinay.viewmodels.GroupTransactionViewModel
import com.example.billbuddy.vinay.viewmodels.GroupViewModel
import com.example.billbuddy.vinay.viewmodels.GroupViewModelFactory
import com.example.billbuddy.vinay.viewmodels.UserViewModel
import com.example.billbuddy.vinay.viewmodels.UserViewModelFactory
import com.example.billbuddy.vinay.views.SplitwiseApplication
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CreateGroupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateGroupBinding
    //viewModels
    private lateinit var userViewModel: UserViewModel
    private lateinit var groupViewModel: GroupViewModel
    private lateinit var friendTransactionViewModel: FriendTransactionViewModel
    private lateinit var transactionViewModel: GroupTransactionViewModel

    var type = "apartment"
    private val preferenceHelper = PreferenceHelper(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createDatabase()
        binding.tvApartment.setOnClickListener {
            type = "apartment"
            binding.tvApartment.setBackgroundColor(Color.parseColor("#1CC29F"))
            binding.tvHouse.setBackgroundColor(Color.parseColor("#4b4b4b"))
            binding.tvTrip.setBackgroundColor(Color.parseColor("#4b4b4b"))
            binding.tvOther.setBackgroundColor(Color.parseColor("#4b4b4b"))
        }
        binding.tvHouse.setOnClickListener {
            type = "house"
            binding.tvHouse.setBackgroundColor(Color.parseColor("#1CC29F"))
            binding.tvApartment.setBackgroundColor(Color.parseColor("#4b4b4b"))
            binding.tvTrip.setBackgroundColor(Color.parseColor("#4b4b4b"))
            binding.tvOther.setBackgroundColor(Color.parseColor("#4b4b4b"))
        }
        binding.tvTrip.setOnClickListener {
            type = "trip"
            binding.tvTrip.setBackgroundColor(Color.parseColor("#1CC29F"))
            binding.tvHouse.setBackgroundColor(Color.parseColor("#4b4b4b"))
            binding.tvApartment.setBackgroundColor(Color.parseColor("#4b4b4b"))
            binding.tvOther.setBackgroundColor(Color.parseColor("#4b4b4b"))
        }
        binding.tvOther.setOnClickListener {
            type = "other"
            binding.tvOther.setBackgroundColor(Color.parseColor("#1CC29F"))
            binding.tvHouse.setBackgroundColor(Color.parseColor("#4b4b4b"))
            binding.tvTrip.setBackgroundColor(Color.parseColor("#4b4b4b"))
            binding.tvApartment.setBackgroundColor(Color.parseColor("#4b4b4b"))
        }

        binding.saveGroup.setOnClickListener {
            val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
            val groupEntity = GroupEntity(
                binding.etGroupName.text.toString(),
                binding.etSize.text.toString().toInt(),
                type,
                0,
                dtf.format(LocalDateTime.now()),
                preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID)
            )
            groupViewModel.addGroup(groupEntity)
            finish()
        }
        binding.tvBackArrow.setOnClickListener {
            finish()
        }
    }


    private fun createDatabase() {
        val appClass = application as SplitwiseApplication
        val userRepository = appClass.userRepository
        //val groupRepository = appClass.groupRepository

        val userViewModelFactory = UserViewModelFactory(userRepository)
        //val groupViewModelFactory = GroupViewModelFactory(groupRepository)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        //groupViewModel = ViewModelProvider(this, groupViewModelFactory)
            //.get(GroupViewModel::class.java)
    }

}