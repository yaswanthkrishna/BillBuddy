package com.example.billbuddy.vinayactivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billbuddy.R
import com.example.billbuddy.databinding.ActivityAddExpenseBinding
import com.example.billbuddy.jing.PercentageSplitFragment
import com.example.billbuddy.jing.NameSelectionFragment
import com.example.billbuddy.jing.OnNameSelectedListener
import com.example.billbuddy.jing.UnequallySplitFragment
import com.example.billbuddy.menubartrail.MenuMainActivity
import com.example.billbuddy.vinay.database.friend_non_group.FriendEntity
import com.example.billbuddy.vinay.database.groups.GroupListEntity
import com.example.billbuddy.vinay.database.groups.GroupMemberEntity
import com.example.billbuddy.vinay.database.groups.GroupTransactionEntity
import com.example.billbuddy.vinay.database.groups.GroupTransactionMemberEntity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.database.transactions.NonGroupTransactionMemberEntity
import com.example.billbuddy.vinay.database.transactions.TransactionEntity
import com.example.billbuddy.vinay.database.users.UserEntity
import com.example.billbuddy.vinay.recyclerviews.ContactCommunicator
import com.example.billbuddy.vinay.recyclerviews.ContactTempAddAdapter
import com.example.billbuddy.vinay.recyclerviews.ContactTempModel
import com.example.billbuddy.vinay.repositories.FriendRepository
import com.example.billbuddy.vinay.repositories.GroupListRepository
import com.example.billbuddy.vinay.repositories.GroupMemberRepository
import com.example.billbuddy.vinay.repositories.GroupTransactionMemberRepository
import com.example.billbuddy.vinay.repositories.GroupTransactionRepository
import com.example.billbuddy.vinay.repositories.TransactionRepository
import com.example.billbuddy.vinay.repositories.UserRepository
import com.example.billbuddy.vinay.viewmodels.FriendViewModel
import com.example.billbuddy.vinay.viewmodels.FriendViewModelFactory
import com.example.billbuddy.vinay.viewmodels.GroupListViewModel
import com.example.billbuddy.vinay.viewmodels.GroupListViewModelFactory
import com.example.billbuddy.vinay.viewmodels.GroupMemberViewModel
import com.example.billbuddy.vinay.viewmodels.GroupMemberViewModelFactory
import com.example.billbuddy.vinay.viewmodels.GroupTransactionMemberViewModel
import com.example.billbuddy.vinay.viewmodels.GroupTransactionMemberViewModelFactory
import com.example.billbuddy.vinay.viewmodels.GroupTransactionViewModel
import com.example.billbuddy.vinay.viewmodels.GroupTransactionViewModelFactory
import com.example.billbuddy.vinay.viewmodels.NonGroupTransactionMemberViewModel
import com.example.billbuddy.vinay.viewmodels.NonGroupTransactionMemberViewModelFactory
import com.example.billbuddy.vinay.viewmodels.TransactionViewModel
import com.example.billbuddy.vinay.viewmodels.TransactionViewModelFactory
import com.example.billbuddy.vinay.viewmodels.UserViewModel
import com.example.billbuddy.vinay.viewmodels.UserViewModelFactory
import com.example.billbuddy.vinay.views.SplitwiseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class GroupExpenseActivity : AppCompatActivity(), ContactCommunicator, OnNameSelectedListener, UnequallySplitFragment.OnAmountsSavedListener,
    PercentageSplitFragment.OnPercentagesSavedListener,NotesFragment.OnNoteEnteredListener{

    private lateinit var binding: ActivityAddExpenseBinding
    private val REQUEST_CODE_CAMERA_PERMISSION = 1
    private var contactList = mutableListOf<ContactTempModel>()
    private var groupList = mutableListOf<ContactTempModel>()
    private var groupsAddedList = mutableListOf<ContactTempModel>()
    private var usersList = mutableListOf<UserEntity>()
    private var groups = mutableListOf<GroupListEntity>()
    private var groupMembers = mutableListOf<GroupMemberEntity>()
    private lateinit var contactAdapter: ContactTempAddAdapter
    private lateinit var to: IntArray
    private val preferenceHelper by lazy { PreferenceHelper(this) }
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var groupListViewModel: GroupListViewModel
    private lateinit var groupMemberViewModel: GroupMemberViewModel
    private lateinit var groupTransactionViewModel: GroupTransactionViewModel
    private lateinit var groupTransactionMemberViewModel: GroupTransactionMemberViewModel
    private var amounts: Map<String, Double> = emptyMap()
    private var percentage: Map<String, Int> = emptyMap()
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var userRepository: UserRepository
    private lateinit var groupListRepository: GroupListRepository
    private lateinit var groupMemberRepository: GroupMemberRepository
    private lateinit var groupTransactionRepository: GroupTransactionRepository
    private lateinit var friendViewModel: FriendViewModel
    private lateinit var groupTransactionMemberRepository: GroupTransactionMemberRepository
    private var notes : String? = null
    private var image_path : String? = null

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val imageBitmap = data?.extras?.get("data") as Bitmap
                val imageUri = saveImageToStorage(imageBitmap)
                saveImagePathToDatabase(imageUri.toString())
            }
        }

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val spinnerSplitType = findViewById<Spinner>(R.id.splittype)

        createDatabase()
        getUsersList()
        getGroupList()

        spinnerSplitType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Handle the selected split type
                val selectedSplitType = parentView?.getItemAtPosition(position).toString()


                Log.d("SpinnerContactList","$contactList")
                // Load the corresponding fragment based on the selected split type
                if ("Unequally" == selectedSplitType) {
                    loadFragment(UnequallySplitFragment(), contactList, binding.tvAmount.text.toString().toDouble())
                } else if ("Percentage" == selectedSplitType) {
                    // Load other fragment for Custom split typ
                    loadFragment(PercentageSplitFragment(), contactList, binding.tvAmount.text.toString().toDouble())
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing
            }
        }


        binding.tvBackArrow.setOnClickListener {
            finish()
        }

        lifecycleScope.launch {
            fetchGroup()
        }

        binding.ibClose.setOnClickListener {
            binding.lvContacts.visibility = View.GONE
            binding.ibClose.visibility = View.GONE
            binding.containerLayout.visibility = View.VISIBLE
        }
        binding.etNameEmailPhone.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.lvContacts.visibility = View.VISIBLE
                binding.ibClose.visibility = View.VISIBLE
                binding.containerLayout.visibility = View.GONE
            } else {
                binding.lvContacts.visibility = View.GONE
                binding.ibClose.visibility = View.GONE
                binding.containerLayout.visibility = View.VISIBLE
            }
        }

        binding.lvContacts.setOnItemClickListener { parent, view, position, id ->
            // Assuming you have a list of ContactTempModel called contactList
            if (position < groupsAddedList.size) {
                val clickedGroup = groupsAddedList[position]

                // Check if the contact is already added
                var flag = true
                for (i in groupList) {
                    if (i.name == clickedGroup.name) {
                        Toast.makeText(this, "Already Added", Toast.LENGTH_SHORT).show()
                        flag = false
                        break
                    }
                }
                // If not added, add it to the contactList and update the adapter
                if (flag) {
                    groupList.add(clickedGroup)
                    contactAdapter = ContactTempAddAdapter(groupList, this)

                    Log.d("GroupExpenseUnique", "groupList:$groupList")

                    for (group in groupList) {
                        val selectedGroup = groups.find { it.groupName == group.name }
                        Log.d("GroupExpenseUnique", "selectedGroup:$selectedGroup")

                        if (selectedGroup != null) {
                            // Use coroutineScope to launch a coroutine
                            CoroutineScope(Dispatchers.Main).launch {
                                val groupMembers = getGroupMembersFromDatabase(selectedGroup.groupId)

                                // Process groupMembers within the coroutine scope
                                Log.d("GroupExpenseUnique", "groupMembers:$groupMembers")

                                for (member in groupMembers) {
                                    val groupuserModel = ContactTempModel(member.userPhone, member.userName)
                                    Log.d("GroupExpenseUnique", "member:$member")

                                    if (!contactList.any { it.number == member.userPhone && it.name == member.userName ?: "" }) {
                                        contactList.add(groupuserModel)
                                        Log.d("GroupExpenseUnique", "contactList:$contactList")
                                    }
                                }

                                // Update the UI after processing groupMembers
                                binding.rvContactAddTemp.layoutManager = LinearLayoutManager(this@GroupExpenseActivity, LinearLayoutManager.HORIZONTAL, false)
                                binding.rvContactAddTemp.adapter = contactAdapter
                            }
                        }
                    }
                }

            }

        }
        binding.addAllUsers.setOnClickListener {
            if (preferenceHelper.readBooleanFromPreference(SplitwiseApplication.PREF_IS_USER_LOGIN)
            ) {
                val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
                val totalAmount = binding.tvAmount.text.toString().toDouble()

                if (totalAmount > 0) {
                    val splitType = binding.splittype.selectedItem.toString()

                    lifecycleScope.launch {
                        if (splitType == "Equally") {
                            handleEquallySplit(totalAmount, dtf)
                        } else if (splitType == "Unequally") {
                            handleUnequallySplit(totalAmount, dtf)
                        } else if (splitType == "Percentage") {
                            handlePercentageSplit(totalAmount, dtf)
                        }
                    }

                } else {
                    Toast.makeText(this, "Invalid Amount", Toast.LENGTH_SHORT).show()
                    val intent2 = Intent(this, MenuMainActivity::class.java)
                    startActivity(intent2)
                    finish()
                }
            }
        }

        binding.cvYou.setOnClickListener {
            val names = contactAdapter.getNamesList()
            showNameSelectionFragment(names)
        }

        val ivNotes = findViewById<ImageView>(R.id.ivNotes)
        ivNotes.setOnClickListener {
            // Show the NotesFragment when the ivNotes is clicked
            showNotesFragment()
        }

        val cameraPermission = Manifest.permission.CAMERA
        val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE



        val ivCamera: ImageView = findViewById(R.id.ivCamera)
        ivCamera.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, cameraPermission) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, storagePermission) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                // Request permissions
                ActivityCompat.requestPermissions(this, arrayOf(cameraPermission, storagePermission), REQUEST_CODE_CAMERA_PERMISSION)
            }
        }
    }

    private fun getCurrentUser(): UserEntity? {

        val currentUser = usersList.firstOrNull { it.user_id.toLong() == preferenceHelper.readLongFromPreference(SplitwiseApplication.PREF_USER_ID) }

        if (currentUser != null) {
            Log.d("AddExpenseActivity", "Current user fetched: ${currentUser.name}, ${currentUser.phone}")
        } else {
            Log.d("AddExpenseActivity", "Current user not found for ID: $preferenceHelper.readLongFromPreference(SplitwiseApplication.PREF_USER_ID)")
        }

        return currentUser
    }

    private fun isCurrentUserInContactList(currentUser: UserEntity): Boolean {
        return contactList.any { it.number == currentUser.phone && it.name == currentUser.name ?: "" }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun handleEquallySplit(totalAmount: Double, dtf: DateTimeFormatter) {

        //val groups = getGroupsFromDatabase()
        var groupid = 0L

        for(group in groupList){
            val selectedGroup = groups.find { it.groupName == group.name }
            if (selectedGroup != null) {
                groupid = selectedGroup.groupId
            }
        }

        /*var groupMembers = getGroupMembersFromDatabase(groupid)

        for(member in groupMembers){
            val groupuserModel = ContactTempModel(member.userPhone, member.userName)
            contactList.add(groupuserModel)
        }

        Log.d("contactList","$contactList")*/
        // Add currentUser to contactList
        val currentUser = usersList.firstOrNull { it.user_id.toLong() == preferenceHelper.readLongFromPreference(SplitwiseApplication.PREF_USER_ID) }

        val numberOfPeople = contactList.size
        val eachShare = totalAmount / numberOfPeople

        val payerName = if (binding.paidbywho.text.toString().equals("You", ignoreCase = true)) {
            // If Paidby is "You," use the current user's name
            currentUser?.name ?: ""
        } else {
            // Otherwise, use the name in the TextView
            binding.paidbywho.text.toString()
        }


        var nextTransactionId = transactionRepository.getNextTransactionId()

        if(nextTransactionId==null){
            nextTransactionId=1
        }
        Log.d("nextTransactionId","$nextTransactionId")


        val transactionEntity = TransactionEntity(
            transactionId = nextTransactionId ?: 0L,
            totalAmount = totalAmount,
            paidByUserId = userViewModel.getUserIdByName(payerName) ?: 0L,
            splitType = binding.splittype.selectedItem.toString(),
            groupFlag = true,
            receiptImage = image_path ?: "",
            comments = null,
            description = binding.tvDescription.text.toString(),
            transactionDateTime = dtf.format(LocalDateTime.now()),
            notes = notes ?: ""
        )
        // Insert the TransactionEntity
        transactionViewModel.addTransaction(transactionEntity)


        val groupTransactionEntity = GroupTransactionEntity(
            transactionId = nextTransactionId,
            groupId= groupid,
            totalAmount = totalAmount
        )

        // Insert the NonGroupTransactionMemberEntity
        groupTransactionViewModel.addGroupTransaction(groupTransactionEntity)

        for (i in contactList) {
            var amountOwes = 0.0
            var amountOwe = 0.0

            if(i.name==payerName){
                amountOwes = totalAmount - eachShare
            }
            else{
                amountOwe=eachShare
            }

            val groupTransactionMemberEntity = GroupTransactionMemberEntity(
                transactionId = nextTransactionId,
                groupId= groupid,
                userId = userViewModel.getUserIdByName(i.name ?: "") ?: 0L,
                amountOwe = amountOwe,
                amountOwes = amountOwes
            )

            // Insert the NonGroupTransactionMemberEntity
            groupTransactionMemberViewModel.addGroupTransactionMember(groupTransactionMemberEntity)

            if (currentUser != null) {
                val frienduserid = userViewModel.getUserIdByName(i.name ?: "") ?: 0L
                if(payerName==currentUser.name){
                    friendViewModel.updateOwesAmount(currentUser.user_id, frienduserid, amountOwe)
                    friendViewModel.updateTotalDue(currentUser.user_id, frienduserid)
                }

                else{
                    if(i.name==payerName){
                        Log.d("currentfriends","inner:False $amountOwes")
                        Log.d("currentfriends","userid:$currentUser.user_id , frienduserid: $frienduserid")
                        friendViewModel.updateOweAmount(currentUser.user_id, frienduserid, eachShare)
                        friendViewModel.updateTotalDue(currentUser.user_id, frienduserid)
                    }
                }
            }
        }

        currentUser?.let {
            if (payerName==currentUser.name) {
                // If the payer is the current user, update the owe amount
                it.owes = (it.owes.toDouble() + (totalAmount - eachShare)).toString()
                groupMemberViewModel.updateGroupOwes(groupid,currentUser.user_id,(totalAmount - eachShare))
                groupMemberViewModel.updateTotalDue(groupid,currentUser.user_id)
            } else {
                // If the payer is someone else, update the owed amount
                it.owe = (it.owe.toDouble() + eachShare).toString()
                groupMemberViewModel.updateUserOwe(groupid,currentUser.user_id,eachShare)
                groupMemberViewModel.updateTotalDue(groupid,currentUser.user_id)
            }
            userViewModel.updateUser(it)
        }


        Toast.makeText(applicationContext, "Group Expense Added", Toast.LENGTH_SHORT).show()

        binding.lvContacts.visibility = View.GONE
        contactList.clear()
        contactAdapter.notifyDataSetChanged()

        val intent2 = Intent(this, MenuMainActivity::class.java)
        startActivity(intent2)
        finish()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun handleUnequallySplit(totalAmount: Double, dtf: DateTimeFormatter) {

        var groupid = 0L

        for(group in groupList){
            val selectedGroup = groups.find { it.groupName == group.name }
            if (selectedGroup != null) {
                groupid = selectedGroup.groupId
            }
        }

        val currentUser = usersList.firstOrNull { it.user_id.toLong() == preferenceHelper.readLongFromPreference(SplitwiseApplication.PREF_USER_ID) }

        val payerName = if (binding.paidbywho.text.toString().equals("You", ignoreCase = true)) {
            // If Paidby is "You," use the current user's name
            val currentUser = usersList.firstOrNull { it.user_id.toLong() == preferenceHelper.readLongFromPreference(SplitwiseApplication.PREF_USER_ID) }
            currentUser?.name ?: ""
        } else {
            // Otherwise, use the name in the TextView
            binding.paidbywho.text.toString()
        }

        var nextTransactionId = transactionRepository.getNextTransactionId()

        if(nextTransactionId==null){
            nextTransactionId=1
        }

        val transactionEntity= TransactionEntity(
            transactionId = nextTransactionId,
            totalAmount=totalAmount,
            paidByUserId=userViewModel.getUserIdByName(payerName) ?: 0L,
            splitType=binding.splittype.selectedItem.toString(),
            groupFlag=true,
            receiptImage = image_path ?: "",
            comments=null,
            description=binding.tvDescription.text.toString(),
            transactionDateTime=dtf.format(LocalDateTime.now()),
            notes = notes ?: ""
        )
        transactionViewModel.addTransaction(transactionEntity)

        val groupTransactionEntity = GroupTransactionEntity(
            transactionId = nextTransactionId,
            groupId= groupid,
            totalAmount = totalAmount
        )

        // Insert the NonGroupTransactionMemberEntity
        groupTransactionViewModel.addGroupTransaction(groupTransactionEntity)

        var currentuserowe = 0.0
        for ((name, amount) in amounts) {


            if (currentUser != null) {
                if(name==currentUser.name){
                    currentuserowe = amount
                    Log.d("currentuserowe",":$currentuserowe")
                }
            }

            var amountOwes = 0.0
            var amountOwe = 0.0

            if(name==payerName){
                amountOwes = totalAmount - amount
            }
            else{
                amountOwe=amount
            }

            val groupTransactionMemberEntity = GroupTransactionMemberEntity(
                transactionId = nextTransactionId,
                groupId= groupid,
                userId = userViewModel.getUserIdByName(name) ?: 0L,
                amountOwe = amountOwe,
                amountOwes = amountOwes
            )
            // Insert the NonGroupTransactionMemberEntity
            groupTransactionMemberViewModel.addGroupTransactionMember(groupTransactionMemberEntity)

            Log.d("groupMemberViewModel","$currentUser")
            if (currentUser != null) {
                val frienduserid = userViewModel.getUserIdByName(name ?: "") ?: 0L

                Log.d("groupMemberViewModel","$frienduserid")
                if(payerName==currentUser.name){
                    friendViewModel.updateOwesAmount(currentUser.user_id, frienduserid, amount)
                    friendViewModel.updateTotalDue(currentUser.user_id, frienduserid)
                    Log.d("groupMemberViewModel","$amount")
                }
            }

        }

        if (currentUser != null) {
            if(payerName!=currentUser.name){
                val payerid = userViewModel.getUserIdByName(payerName ?: "") ?: 0L
                friendViewModel.updateOweAmount(currentUser.user_id, payerid, currentuserowe)
                friendViewModel.updateTotalDue(currentUser.user_id, payerid)

            }
        }

        currentUser?.let {
            if (binding.paidbywho.text.toString().equals("You", ignoreCase = true)) {
                // If the payer is the current user, update the owe amount
                val currentUserAmount = amounts[it.name] ?: 0.0 // Default to 0 if it.name is not found in amounts
                it.owes = (it.owes.toDouble() + (totalAmount - currentUserAmount)).toString()
                groupMemberViewModel.updateGroupOwes(groupid,currentUser.user_id,(totalAmount - currentUserAmount))
                groupMemberViewModel.updateTotalDue(groupid,currentUser.user_id)
            } else {
                // If the payer is someone else, update the owed amount
                val currentUserAmount = amounts[it.name] ?: 0.0
                it.owe = (it.owe.toDouble() + currentUserAmount).toString()
                groupMemberViewModel.updateUserOwe(groupid,currentUser.user_id,currentUserAmount)
                groupMemberViewModel.updateTotalDue(groupid,currentUser.user_id)

            }
            userViewModel.updateUser(it)
        }

        Toast.makeText(applicationContext, "Group Expense Added", Toast.LENGTH_SHORT).show()

        binding.lvContacts.visibility = View.GONE
        contactList.clear()
        contactAdapter.notifyDataSetChanged()

        val intent2 = Intent(this, MenuMainActivity::class.java)
        startActivity(intent2)
        finish()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun handlePercentageSplit(totalAmount: Double, dtf: DateTimeFormatter) {

        var groupid = 0L

        for(group in groupList){
            val selectedGroup = groups.find { it.groupName == group.name }
            if (selectedGroup != null) {
                groupid = selectedGroup.groupId
            }
        }

        // Get the list of selected persons and their assigned percentages
        val currentUser = usersList.firstOrNull { it.user_id.toLong() == preferenceHelper.readLongFromPreference(SplitwiseApplication.PREF_USER_ID) }

        val percentages = percentage

        val payerName = if (binding.paidbywho.text.toString().equals("You", ignoreCase = true)) {
            // If Paidby is "You," use the current user's name
            val currentUser = usersList.firstOrNull { it.user_id.toLong() == preferenceHelper.readLongFromPreference(SplitwiseApplication.PREF_USER_ID) }
            currentUser?.name ?: ""
        } else {
            // Otherwise, use the name in the TextView
            binding.paidbywho.text.toString()
        }

        var nextTransactionId = transactionRepository.getNextTransactionId()

        if(nextTransactionId==null){
            nextTransactionId=1
        }

        val transactionEntity= TransactionEntity(
            transactionId = nextTransactionId,
            totalAmount=totalAmount,
            paidByUserId=userViewModel.getUserIdByName(payerName) ?: 0L,
            splitType=binding.splittype.selectedItem.toString(),
            groupFlag=true,
            receiptImage = image_path ?: "",
            comments=null,
            description=binding.tvDescription.text.toString(),
            transactionDateTime=dtf.format(LocalDateTime.now()),
            notes = notes ?: ""
        )
        transactionViewModel.addTransaction(transactionEntity)

        val groupTransactionEntity = GroupTransactionEntity(
            transactionId = nextTransactionId,
            groupId= groupid,
            totalAmount = totalAmount
        )

        // Insert the NonGroupTransactionMemberEntity
        groupTransactionViewModel.addGroupTransaction(groupTransactionEntity)

        var currentuserowe = 0.0
        for (person in contactList) {
            val name = person.name ?: ""

            // Get the percentage assigned to the current person
            val personPercentage = percentages[name] ?: 0

            // Calculate the amount for the current person based on the percentage
            val amount = (totalAmount * personPercentage) / 100

            if (currentUser != null) {
                if(name==currentUser.name){
                    currentuserowe = amount
                    Log.d("currentuserowe",":$currentuserowe")
                }
            }

            var amountOwes = 0.0
            var amountOwe = 0.0

            if(name==payerName){
                amountOwes = totalAmount - amount
            }
            else{
                amountOwe=amount
            }

            val groupTransactionMemberEntity = GroupTransactionMemberEntity(
                transactionId = nextTransactionId,
                userId = userViewModel.getUserIdByName(name) ?: 0L,
                groupId = groupid,
                amountOwe = amountOwe,
                amountOwes = amountOwes
            )
            // Insert the NonGroupTransactionMemberEntity
            groupTransactionMemberViewModel.addGroupTransactionMember(groupTransactionMemberEntity)

            if (currentUser != null) {
                val frienduserid = userViewModel.getUserIdByName(name ?: "") ?: 0L

                if(payerName==currentUser.name){
                    friendViewModel.updateOwesAmount(currentUser.user_id, frienduserid, amount)
                    friendViewModel.updateTotalDue(currentUser.user_id, frienduserid)
                }
            }
        }

        if (currentUser != null) {
            if(payerName!=currentUser.name){
                val payerid = userViewModel.getUserIdByName(payerName ?: "") ?: 0L
                friendViewModel.updateOweAmount(currentUser.user_id, payerid, currentuserowe)
                friendViewModel.updateTotalDue(currentUser.user_id, payerid)
            }
        }


        Log.d("percentageSplit","CurrentUser:$currentUser")
        currentUser?.let {
            val currentUserPercentage = percentage[it.name] ?: 0

            // Calculate the amount for the current user based on the percentage
            val currentUserAmount = (totalAmount * currentUserPercentage) / 100

            if (binding.paidbywho.text.toString().equals("You", ignoreCase = true)) {
                // If the payer is the current user, update the owe amount
                val currentUserAmount = amounts[it.name] ?: 0.0 // Default to 0 if it.name is not found in amounts
                it.owes = (it.owes.toDouble() + (totalAmount - currentUserAmount)).toString()
                groupMemberViewModel.updateGroupOwes(groupid,currentUser.user_id,(totalAmount - currentUserAmount))
                groupMemberViewModel.updateTotalDue(groupid,currentUser.user_id)
            } else {
                // If the payer is someone else, update the owed amount
                it.owe = (it.owe.toDouble() + currentUserAmount).toString()
                groupMemberViewModel.updateUserOwe(groupid,currentUser.user_id,currentUserAmount)
                groupMemberViewModel.updateTotalDue(groupid,currentUser.user_id)
            }
            userViewModel.updateUser(it)
        }

        Toast.makeText(applicationContext, "Group Expense Added", Toast.LENGTH_SHORT).show()

        binding.lvContacts.visibility = View.GONE
        contactList.clear()
        contactAdapter.notifyDataSetChanged()

        val intent2 = Intent(this, MenuMainActivity::class.java)
        startActivity(intent2)
        finish()
    }


    private fun getUsersList() {
        userViewModel.getUserList().observe(this, Observer {
            usersList.clear()
            usersList.addAll(it)
        })
    }

    private fun getGroupList() {
        groupListViewModel.getGroupList().observe(this, Observer {
            groups.clear()
            groups.addAll(it)
        })
    }

    private fun getGroupMember(groupId:Long) {
        groupMemberViewModel.getGroupMembers(groupId).observe(this, Observer {
            groupMembers.clear()
            groupMembers.addAll(it)
        })
    }


    private fun loadFragment(fragment: Fragment, selectedPersons: List<ContactTempModel>,amount: Double) {
        val bundle = Bundle()
        bundle.putParcelableArrayList("selectedPersons", ArrayList(selectedPersons))
        bundle.putDouble("amount", amount)
        fragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    private suspend fun fetchGroup(){
        delay(50)
        val groupList = getGroupsFromDatabase()
        Log.d("fetchFriends", "$groupList")

        groupsAddedList.clear()

        for (group in groupList) {
            val contactTempModel = ContactTempModel("", group.groupName)
            groupsAddedList.add(contactTempModel)
        }

        // Create a SimpleAdapter for the ListView
        val adapter = SimpleAdapter(
            this,
            groupsAddedList.map { mapOf("name" to it.name, "phone" to it.number) },
            android.R.layout.simple_list_item_2,
            arrayOf("name", "phone"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )

        // Set the adapter to the ListView
        withContext(Dispatchers.Main) {
            binding.lvContacts.adapter = adapter
        }
    }


    private fun createDatabase() {
        val appClass = application as SplitwiseApplication

        userRepository = appClass.userRepository
        val userViewModelFactory = UserViewModelFactory(userRepository)

        transactionRepository = appClass.transactionRepository
        val transactionViewModelFactory = TransactionViewModelFactory(transactionRepository)

        groupListRepository = appClass.groupListRepository
        val groupListViewModelFactory = GroupListViewModelFactory(groupListRepository)

        groupMemberRepository = appClass.groupMemberRepository
        val groupMemberViewModelFactory = GroupMemberViewModelFactory(groupMemberRepository)

        groupTransactionRepository = appClass.groupTransactionRepository
        val groupTransactionViewModelFactory = GroupTransactionViewModelFactory(groupTransactionRepository)

        groupTransactionMemberRepository = appClass.groupTransactionMemberRepository
        val groupTransactionMemberViewModelFactory = GroupTransactionMemberViewModelFactory(groupTransactionMemberRepository)

        val friendRepository = appClass.friendRepository
        val friendViewModelFactory = FriendViewModelFactory(friendRepository)


        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        transactionViewModel = ViewModelProvider(this, transactionViewModelFactory)
            .get(TransactionViewModel::class.java)

        groupListViewModel = ViewModelProvider(this, groupListViewModelFactory)
            .get(GroupListViewModel::class.java)

        groupMemberViewModel = ViewModelProvider(this, groupMemberViewModelFactory)
            .get(GroupMemberViewModel::class.java)

        groupTransactionViewModel = ViewModelProvider(this, groupTransactionViewModelFactory)
            .get(GroupTransactionViewModel::class.java)

        groupTransactionMemberViewModel = ViewModelProvider(this, groupTransactionMemberViewModelFactory)
            .get(GroupTransactionMemberViewModel::class.java)

        friendViewModel = ViewModelProvider(this, friendViewModelFactory)
            .get(FriendViewModel::class.java)

    }

    private suspend fun getGroupsFromDatabase(): List<GroupListEntity> = suspendCoroutine { continuation ->

        val groupListLiveData = groupListViewModel.getGroupList()
        Log.d("FetchGroups", "GroupListLiveData: $groupListLiveData")

        groupListLiveData?.observeOnce(this) { groupList ->
            Log.d("FetchGroups", "GroupList: $groupList")
            continuation.resume(groupList)
        }
    }

    private suspend fun getGroupMembersFromDatabase(groupId:Long): List<GroupMemberEntity> = suspendCoroutine { continuation ->

        val groupMemberLiveData = groupMemberViewModel.getGroupMembers(groupId)
        Log.d("FetchGroups", "GroupMemberLiveData: $groupMemberLiveData")

        groupMemberLiveData?.observeOnce(this) { groupMember ->
            Log.d("FetchGroups", "GroupList: $groupMember")
            continuation.resume(groupMember)
        }
    }

    fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>) {
        observe(owner, object : Observer<T> {
            override fun onChanged(value: T) {
                observer.onChanged(value)
                removeObserver(this)
            }
        })
    }

    override fun onContactDelete(tempModel: ContactTempModel) {
        var x = 0
        for (i in groupList) {
            if (i.name == tempModel.name) {
                groupList.removeAt(x)
                break;
            }
            x++
        }
        contactAdapter.notifyDataSetChanged()
    }

    private fun showNameSelectionFragment(names: List<String>) {
        val nameSelectionFragment = NameSelectionFragment.newInstance(names)

        // Set the listener to receive the selected name
        nameSelectionFragment.setOnNameSelectedListener(this)

        // Replace the main container with the fragment layout
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, nameSelectionFragment)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    override fun onNameSelected(name: String) {
        // Handle the selected name here
        binding.paidbywho.text = Editable.Factory.getInstance().newEditable(name)

        // Close the fragment
        supportFragmentManager.popBackStack()
    }

    override fun onAmountsSaved(amounts: Map<String, Double>) {
        Log.d("OnAmountSaved","$amounts")
        this.amounts =amounts
    }

    override fun onPercentagesSaved(percentages: Map<String, Int>) {
        Log.d("onPercentagesSaved","$percentages")
        this.percentage=percentages
    }
    private fun showNotesFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val notesFragment = NotesFragment()
        notesFragment.show(fragmentTransaction, NotesFragment::class.java.simpleName)
    }

    // Implement the interface method to receive notes from the NotesFragment
    override fun onNoteEntered(note: String) {
        // Handle the entered notes, for example, store them in the database
        Log.d("AddExpenseActivity", "Entered Notes: $note")
        notes=note

        // Dismiss the fragment after handling the notes
        val notesFragment = supportFragmentManager.findFragmentByTag(NotesFragment::class.java.simpleName)
        if (notesFragment != null) {
            (notesFragment as DialogFragment).dismiss()
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            takePictureLauncher.launch(takePictureIntent)
        }
    }

    private fun saveImageToStorage(imageBitmap: Bitmap): Uri {
        val imageFileName = "JPEG_${System.currentTimeMillis()}.jpg"
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        try {
            contentResolver.openOutputStream(imageUri!!)?.use {
                it.write(bitmapToByteArray(imageBitmap))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return imageUri!!
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    private fun saveImagePathToDatabase(imagePath: String) {
        Log.d("Image Path","$imagePath")
        // Insert the image path into the database
        // Use your database helper or ORM here
        // Example: dbHelper.insertImagePath(imagePath)
        image_path=imagePath
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, launch the camera intent
                dispatchTakePictureIntent()
            } else {
                // Permission denied, handle accordingly (e.g., show a message or request again)
            }
        }
    }

}