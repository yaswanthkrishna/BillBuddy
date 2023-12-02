package com.example.billbuddy.vinayactivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.billbuddy.R
import com.example.billbuddy.databinding.ActivityAddExpenseBinding
import com.example.billbuddy.jing.PercentageSplitFragment
import com.example.billbuddy.jing.NameSelectionFragment
import com.example.billbuddy.jing.OnNameSelectedListener
import com.example.billbuddy.jing.UnequallySplitFragment
import com.example.billbuddy.menubartrail.MenuMainActivity
import com.example.billbuddy.vinay.database.SplitwiseDatabase
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.database.transactions.NonGroupTransactionMemberEntity
import com.example.billbuddy.vinay.database.transactions.TransactionDAO
import com.example.billbuddy.vinay.database.transactions.TransactionEntity
import com.example.billbuddy.vinay.database.users.UserEntity
import com.example.billbuddy.vinay.recyclerviews.ContactCommunicator
import com.example.billbuddy.vinay.recyclerviews.ContactTempAddAdapter
import com.example.billbuddy.vinay.recyclerviews.ContactTempModel
import com.example.billbuddy.vinay.repositories.TransactionRepository
import com.example.billbuddy.vinay.viewmodels.NonGroupTransactionMemberViewModel
import com.example.billbuddy.vinay.viewmodels.NonGroupTransactionMemberViewModelFactory
import com.example.billbuddy.vinay.viewmodels.TransactionViewModel
import com.example.billbuddy.vinay.viewmodels.TransactionViewModelFactory
import com.example.billbuddy.vinay.viewmodels.UserViewModel
import com.example.billbuddy.vinay.viewmodels.UserViewModelFactory
import com.example.billbuddy.vinay.views.SplitwiseApplication
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AddExpenseActivity : AppCompatActivity(), ContactCommunicator, OnNameSelectedListener, UnequallySplitFragment.OnAmountsSavedListener,
    PercentageSplitFragment.OnPercentagesSavedListener{

    private lateinit var binding: ActivityAddExpenseBinding
    private val CONTACT_PERMISSION_REQUEST_CODE = 1
    private lateinit var cursor: Cursor
    private var contactList = mutableListOf<ContactTempModel>()
    private var usersList = mutableListOf<UserEntity>()
    private lateinit var contactAdapter: ContactTempAddAdapter
    private lateinit var to: IntArray
    private val preferenceHelper by lazy { PreferenceHelper(this) }
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var nonGroupTransactionMemberViewModel: NonGroupTransactionMemberViewModel
    private var amounts: Map<String, Double> = emptyMap()
    private var percentage: Map<String, Int> = emptyMap()
    private lateinit var transactionRepository: TransactionRepository

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val spinnerSplitType = findViewById<Spinner>(R.id.splittype)

        spinnerSplitType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Handle the selected split type
                val selectedSplitType = parentView?.getItemAtPosition(position).toString()

                // Load the corresponding fragment based on the selected split type
                if ("Unequally" == selectedSplitType) {
                    val currentUser = getCurrentUser()

                    if (currentUser != null && !isCurrentUserInContactList(currentUser)) {
                        val currentUserModel = ContactTempModel(currentUser.phone, currentUser.name ?: "")
                        contactList.add(currentUserModel)
                    }

                    loadFragment(UnequallySplitFragment(), contactList, binding.tvAmount.text.toString().toDouble())
                } else if ("Percentage" == selectedSplitType) {
                    val currentUser = getCurrentUser()
                    if (currentUser != null && !isCurrentUserInContactList(currentUser)) {
                        val currentUserModel = ContactTempModel(currentUser.phone, currentUser.name ?: "")
                        contactList.add(currentUserModel)
                    }
                    // Load other fragment for Custom split typ
                    loadFragment(PercentageSplitFragment(), contactList, binding.tvAmount.text.toString().toDouble())
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing
            }
        }

        createDatabase()
        getUsersList()

        binding.tvBackArrow.setOnClickListener {
            finish()
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
            ),
            CONTACT_PERMISSION_REQUEST_CODE
        )
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
            var cTM =
                ContactTempModel(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
                    cursor.getString(cursor.getColumnIndex("DISPLAY_NAME"))
                )
            var flag = true
            for (i in contactList) {
                if (i.name == cTM.name && i.number == cTM.number) {
                    Toast.makeText(this, "Already Added", Toast.LENGTH_SHORT).show()
                    flag = false
                    break;
                }
            }
            if (flag) {
                contactList.add(cTM)
                contactAdapter =
                    ContactTempAddAdapter(
                        contactList,
                        this
                    )
                binding.rvContactAddTemp.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                binding.rvContactAddTemp.adapter = contactAdapter
            }
        }
        binding.addAllUsers.setOnClickListener {
            if (contactList.isNotEmpty() &&
                preferenceHelper.readBooleanFromPreference(SplitwiseApplication.PREF_IS_USER_LOGIN)
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
    }

    private fun getCurrentUser(): UserEntity? {

        val currentUser = usersList.firstOrNull { it.user_id == preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID) }

        if (currentUser != null) {
            Log.d("AddExpenseActivity", "Current user fetched: ${currentUser.name}, ${currentUser.phone}")
        } else {
            Log.d("AddExpenseActivity", "Current user not found for ID: $preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID)")
        }

        return currentUser
    }

    private fun isCurrentUserInContactList(currentUser: UserEntity): Boolean {
        return contactList.any { it.number == currentUser.phone && it.name == currentUser.name ?: "" }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun handleEquallySplit(totalAmount: Double, dtf: DateTimeFormatter) {
        // Add currentUser to contactList
        val currentUser = usersList.firstOrNull { it.user_id == preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID) }
        currentUser?.let {
            val currentUserModel = ContactTempModel(it.phone, it.name ?: "")
            contactList.add(currentUserModel)
        }

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
            groupFlag = false,
            receiptImage = null,
            comments = null,
            description = binding.tvDescription.text.toString(),
            transactionDateTime = dtf.format(LocalDateTime.now())
        )

        // Insert the TransactionEntity
        transactionViewModel.addTransaction(transactionEntity)

        for (i in contactList) {
            var amountOwes = 0.0
            var amountOwe = 0.0

            if(i.name==payerName){
                amountOwes = totalAmount - eachShare
            }
            else{
                amountOwe=eachShare
            }
            val nonGroupTransactionMemberEntity = NonGroupTransactionMemberEntity(
                transactionId = nextTransactionId ?: 0L,
                userId = userViewModel.getUserIdByName(i.name ?: "") ?: 0L,
                amountOwe = amountOwe,
                amountOwes = amountOwes
            )

            // Insert the NonGroupTransactionMemberEntity
            nonGroupTransactionMemberViewModel.addTransactionMember(nonGroupTransactionMemberEntity)
        }

        currentUser?.let {
            if (binding.paidbywho.text.toString().equals("You", ignoreCase = true)) {
                // If the payer is the current user, update the owe amount
                it.owes = (it.owes.toDouble() + (totalAmount - eachShare)).toString()
            } else {
                // If the payer is someone else, update the owed amount
                it.owe = (it.owe.toDouble() + eachShare).toString()
            }
            userViewModel.updateUser(it)
        }


        Toast.makeText(applicationContext, "Friends Added", Toast.LENGTH_SHORT).show()

        binding.lvContacts.visibility = View.GONE
        contactList.clear()
        contactAdapter.notifyDataSetChanged()

        val intent2 = Intent(this, MenuMainActivity::class.java)
        startActivity(intent2)
        finish()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun handleUnequallySplit(totalAmount: Double, dtf: DateTimeFormatter) {

        val payerName = if (binding.paidbywho.text.toString().equals("You", ignoreCase = true)) {
            // If Paidby is "You," use the current user's name
            val currentUser = usersList.firstOrNull { it.user_id == preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID) }
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
            groupFlag=false,
            receiptImage=null,
            comments=null,
            description=binding.tvDescription.text.toString(),
            transactionDateTime=dtf.format(LocalDateTime.now())
        )
        transactionViewModel.addTransaction(transactionEntity)


        for ((name, amount) in amounts) {
            val phoneNumber = contactList.firstOrNull { it.name == name }?.number

            var amountOwes = 0.0
            var amountOwe = 0.0

            if(name==payerName){
                amountOwes = totalAmount - amount
            }
            else{
                amountOwe=amount
            }
            val nonGroupTransactionMemberEntity = NonGroupTransactionMemberEntity(
                transactionId = nextTransactionId,
                userId = userViewModel.getUserIdByName(name) ?: 0L,
                amountOwe = amountOwe,
                amountOwes = amountOwes
            )
            // Insert the NonGroupTransactionMemberEntity
            nonGroupTransactionMemberViewModel.addTransactionMember(nonGroupTransactionMemberEntity)

        }

        val currentUser = usersList.firstOrNull { it.user_id == preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID) }
        currentUser?.let {
            if (binding.paidbywho.text.toString().equals("You", ignoreCase = true)) {
                // If the payer is the current user, update the owe amount
                val currentUserAmount = amounts[it.name] ?: 0.0 // Default to 0 if it.name is not found in amounts
                it.owes = (it.owes.toDouble() + (totalAmount - currentUserAmount)).toString()
            } else {
                // If the payer is someone else, update the owed amount
                val currentUserAmount = amounts[it.name] ?: 0.0
                it.owe = (it.owe.toDouble() + currentUserAmount).toString()
            }
            userViewModel.updateUser(it)
        }

        Toast.makeText(applicationContext, "Friends Added", Toast.LENGTH_SHORT).show()

        binding.lvContacts.visibility = View.GONE
        contactList.clear()
        contactAdapter.notifyDataSetChanged()

        val intent2 = Intent(this, MenuMainActivity::class.java)
        startActivity(intent2)
        finish()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun handlePercentageSplit(totalAmount: Double, dtf: DateTimeFormatter) {
        // Get the list of selected persons and their assigned percentages
        val percentages = percentage

        val payerName = if (binding.paidbywho.text.toString().equals("You", ignoreCase = true)) {
            // If Paidby is "You," use the current user's name
            val currentUser = usersList.firstOrNull { it.user_id == preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID) }
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
            groupFlag=false,
            receiptImage=null,
            comments=null,
            description=binding.tvDescription.text.toString(),
            transactionDateTime=dtf.format(LocalDateTime.now())
        )
        transactionViewModel.addTransaction(transactionEntity)

        for (person in contactList) {
            val name = person.name ?: ""
            val phoneNumber = person.number ?: ""

            // Get the percentage assigned to the current person
            val personPercentage = percentages[name] ?: 0

            // Calculate the amount for the current person based on the percentage
            val amount = (totalAmount * personPercentage) / 100

            var amountOwes = 0.0
            var amountOwe = 0.0

            if(name==payerName){
                amountOwes = totalAmount - amount
            }
            else{
                amountOwe=amount
            }
            val nonGroupTransactionMemberEntity = NonGroupTransactionMemberEntity(
                transactionId = nextTransactionId,
                userId = userViewModel.getUserIdByName(name) ?: 0L,
                amountOwe = amountOwe,
                amountOwes = amountOwes
            )
            // Insert the NonGroupTransactionMemberEntity
            nonGroupTransactionMemberViewModel.addTransactionMember(nonGroupTransactionMemberEntity)

        }

        val currentUser = usersList.firstOrNull { it.user_id == preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID) }
        currentUser?.let {
            val totalPercentage = percentage.values.sum()
            val currentUserPercentage = percentage[it.name] ?: 0

            // Calculate the amount for the current user based on the percentage
            val currentUserAmount = (totalAmount * currentUserPercentage) / 100

            if (binding.paidbywho.text.toString().equals("You", ignoreCase = true)) {
                // If the payer is the current user, update the owe amount
                val currentUserAmount = amounts[it.name] ?: 0.0 // Default to 0 if it.name is not found in amounts
                it.owes = (it.owes.toDouble() + (totalAmount - currentUserAmount)).toString()
            } else {
                // If the payer is someone else, update the owed amount
                it.owe = (it.owe.toDouble() + currentUserAmount).toString()
            }
            userViewModel.updateUser(it)
        }

        Toast.makeText(applicationContext, "Friends Added", Toast.LENGTH_SHORT).show()

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



    private fun fetchContacts() {

        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        this.cursor = cursor!!
        startManagingCursor(cursor)

        val from = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone._ID
        )

        to = IntArray(2)
        to[0] = android.R.id.text1
        to[1] = android.R.id.text2

        val simpleCursorAdapter =
            SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to)

        binding.lvContacts.adapter = simpleCursorAdapter
        binding.lvContacts.choiceMode = ListView.CHOICE_MODE_MULTIPLE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CONTACT_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchContacts()
                } else {
                    Toast.makeText(
                        this,
                        "Contact Permission Denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun createDatabase() {
        val appClass = application as SplitwiseApplication

        transactionRepository = appClass.transactionRepository
        val transactionViewModelFactory = TransactionViewModelFactory(transactionRepository)

        val userRepository = appClass.userRepository
        val userViewModelFactory = UserViewModelFactory(userRepository)

        val nonGroupTransactionMemberRepository = appClass.nongroupTransactionMemberRepository
        val nonGroupTransactionMemberViewModelFactory = NonGroupTransactionMemberViewModelFactory(nonGroupTransactionMemberRepository)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        transactionViewModel = ViewModelProvider(this, transactionViewModelFactory)
            .get(TransactionViewModel::class.java)

        nonGroupTransactionMemberViewModel = ViewModelProvider(this, nonGroupTransactionMemberViewModelFactory)
            .get(NonGroupTransactionMemberViewModel::class.java)
    }

    override fun onContactDelete(tempModel: ContactTempModel) {
        var x = 0
        for (i in contactList) {
            if (i.name == tempModel.name && i.number == tempModel.number) {
                contactList.removeAt(x)
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


}