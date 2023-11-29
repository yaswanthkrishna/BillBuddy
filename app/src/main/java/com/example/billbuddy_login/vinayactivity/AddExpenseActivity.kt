package com.example.billbuddy_login.vinayactivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
<<<<<<< Updated upstream:app/src/main/java/com/example/billbuddy_login/vinayactivity/AddExpenseActivity.kt
import com.example.billbuddy_login.R
=======
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billbuddy.R
import com.example.billbuddy.databinding.ActivityAddExpenseBinding
import com.example.billbuddy.jing.PercentageSplitFragment
import com.example.billbuddy.jing.NameSelectionFragment
import com.example.billbuddy.jing.OnNameSelectedListener
import com.example.billbuddy.jing.UnequallySplitFragment
import com.example.billbuddy.menubartrail.MenuMainActivity
import com.example.billbuddy.vinay.database.friend_non_group.FriendTransactionEntity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.database.users.UserEntity
import com.example.billbuddy.vinay.recyclerviews.ContactCommunicator
import com.example.billbuddy.vinay.recyclerviews.ContactTempAddAdapter
import com.example.billbuddy.vinay.recyclerviews.ContactTempModel
import com.example.billbuddy.vinay.viewmodels.FriendTransactionViewModel
import com.example.billbuddy.vinay.viewmodels.FriendTransactionViewModelFactory
import com.example.billbuddy.vinay.viewmodels.UserViewModel
import com.example.billbuddy.vinay.viewmodels.UserViewModelFactory
import com.example.billbuddy.vinay.views.SplitwiseApplication
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
>>>>>>> Stashed changes:app/src/main/java/com/example/billbuddy/vinayactivity/AddExpenseActivity.kt


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
    private lateinit var friendTransactionViewModel: FriendTransactionViewModel
    private lateinit var userViewModel: UserViewModel
    private var amounts: Map<String, Int> = emptyMap()
    private var percentage: Map<String, Int> = emptyMap()

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val spinnerSplitType = findViewById<Spinner>(R.id.splittype)
        val fragmentContainer = findViewById<FrameLayout>(R.id.fragmentContainer)

        spinnerSplitType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                // Handle the selected split type
                val selectedSplitType = parentView?.getItemAtPosition(position).toString()

                // Load the corresponding fragment based on the selected split type
                if ("Unequally" == selectedSplitType) {
                    Log.d("contactList calling Unequally","$contactList")
                    loadFragment(UnequallySplitFragment(), contactList, binding.tvAmount.text.toString().toInt())
                } else if ("Percentage" == selectedSplitType) {
                    // Load other fragment for Custom split typ
                    loadFragment(PercentageSplitFragment(), contactList, binding.tvAmount.text.toString().toInt())
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing
            }
        }


        createDatabase()
        getUsersList()

        val currentUser = getCurrentUser()
        if (currentUser != null && !isCurrentUserInContactList(currentUser)) {
            val currentUserModel = ContactTempModel(currentUser.phone, currentUser.name ?: "")
            contactList.add(currentUserModel)
            Log.d("contactList","$contactList")
        }
        
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
                val totalAmount = binding.tvAmount.text.toString().toInt()

                if (totalAmount > 0) {
                    val splitType = binding.splittype.selectedItem.toString()

                    if (splitType == "Equally") {
                        handleEquallySplit(totalAmount, dtf)
                    } else if (splitType == "Unequally") {
                        handleUnequallySplit(totalAmount, dtf)
                    } else if (splitType == "Percentage"){
                        handlePercentageSplit(totalAmount, dtf)
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
        Log.d("PREF_USER_ID", "currentUser: $SplitwiseApplication.PREF_USER_ID")
        val currentUser = usersList.firstOrNull { it.id == preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID) }
        Log.d("getCurrentUser", "$currentUser")
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
    private fun handleEquallySplit(totalAmount: Int, dtf: DateTimeFormatter) {
        val numberOfPeople = contactList.size + 1
        val eachShare = totalAmount / numberOfPeople

        for (i in contactList) {
            val friendTransactionEntity = FriendTransactionEntity(
                preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID),
                i.name ?: "",
                i.number ?: "",
                eachShare,
                dtf.format(LocalDateTime.now()),
                binding.tvDescription.text.toString(),
                binding.paidbywho.text.toString(),
                binding.splittype.selectedItem.toString()
            )
            friendTransactionViewModel.addFriendTransaction(friendTransactionEntity)
        }

        // Update the user's owed amount
        val currentUser = usersList.firstOrNull { it.id == preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID) }
        Log.d("handleEquallySplit", "$currentUser")
        currentUser?.let {
            it.owe = (it.owe.toInt() + (totalAmount - eachShare)).toString()
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
    private fun handleUnequallySplit(totalAmount: Int, dtf: DateTimeFormatter) {
        for ((name, amount) in amounts) {
            val phoneNumber = contactList.firstOrNull { it.name == name }?.number
            val friendTransactionEntity = FriendTransactionEntity(
                preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID),
                name,
                phoneNumber ?: "", // If phoneNumber is null, default to an empty string
                amount,
                dtf.format(LocalDateTime.now()),
                binding.tvDescription.text.toString(),
                binding.paidbywho.text.toString(),
                binding.splittype.selectedItem.toString()
            )
            friendTransactionViewModel.addFriendTransaction(friendTransactionEntity)
        }

        // Update the user's owed amount
        val currentUser = usersList.firstOrNull { it.id == preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID) }
        currentUser?.let {
            it.owe = (it.owe.toInt() + (totalAmount - amounts.values.sum())).toString()
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
    private fun handlePercentageSplit(totalAmount: Int, dtf: DateTimeFormatter) {
        // Get the list of selected persons and their assigned percentages
        val percentages = percentage

        for (person in contactList) {
            val name = person.name ?: ""
            val phoneNumber = person.number ?: ""

            // Get the percentage assigned to the current person
            val personPercentage = percentages[name] ?: 0

            // Calculate the amount for the current person based on the percentage
            val amount = (totalAmount * personPercentage) / 100

            // Create FriendTransactionEntity and add to the ViewModel
            val friendTransactionEntity = FriendTransactionEntity(
                preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID),
                name,
                phoneNumber,
                amount,
                dtf.format(LocalDateTime.now()),
                binding.tvDescription.text.toString(),
                binding.paidbywho.text.toString(),
                binding.splittype.selectedItem.toString()
            )
            friendTransactionViewModel.addFriendTransaction(friendTransactionEntity)
        }

        // Update the user's owed amount
        val currentUser = usersList.firstOrNull { it.id == preferenceHelper.readIntFromPreference(SplitwiseApplication.PREF_USER_ID) }
        currentUser?.let {
            it.owe = (it.owe.toInt() + (totalAmount - percentage.values.sum())).toString()
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

    private fun loadFragment(fragment: Fragment, selectedPersons: List<ContactTempModel>,amount: Int) {
        val bundle = Bundle()
        bundle.putParcelableArrayList("selectedPersons", ArrayList(selectedPersons))
        bundle.putInt("amount", amount)
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
        val friendRepository = appClass.friendsRepository
        val friendViewModelFactory = FriendTransactionViewModelFactory(friendRepository)
        val userRepository = appClass.userRepository
        val userViewModelFactory = UserViewModelFactory(userRepository)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)
        friendTransactionViewModel = ViewModelProvider(this, friendViewModelFactory)
            .get(FriendTransactionViewModel::class.java)
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

    override fun onAmountsSaved(amounts: Map<String, Int>) {
        Log.d("OnAmountSaved","$amounts")
        this.amounts =amounts
    }

    override fun onPercentagesSaved(percentages: Map<String, Int>) {
        Log.d("onPercentagesSaved","$percentages")
        this.percentage=percentages
    }


}