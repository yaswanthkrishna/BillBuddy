package com.example.billbuddy.vinayactivity

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.database.Cursor
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.versionedparcelable.VersionedParcelize
import com.example.billbuddy.R
import com.example.billbuddy.vinay.database.friend_non_group.FriendEntity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.database.users.UserEntity
import com.example.billbuddy.vinay.viewmodels.FriendViewModel
import com.example.billbuddy.vinay.viewmodels.FriendViewModelFactory
import com.example.billbuddy.vinay.viewmodels.UserViewModel
import com.example.billbuddy.vinay.viewmodels.UserViewModelFactory
import com.example.billbuddy.vinay.views.SplitwiseApplication
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
class AddFriendActivity : AppCompatActivity() {

    private val CONTACTS_PERMISSION_REQUEST = 1
    private lateinit var recyclerView: RecyclerView
    private lateinit var selectedRecyclerView: RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter
    private lateinit var selectedContactsAdapter: SelectedContactsAdapter
    private lateinit var etSearch: EditText
    private var allContacts: List<Contact> = emptyList()
    private var selectedContacts: MutableList<Contact> = mutableListOf()
    private lateinit var friendViewModel: FriendViewModel
    private lateinit var userViewModel: UserViewModel
    private var usersList = mutableListOf<UserEntity>()
    private var friendList = mutableListOf<FriendEntity>()
    private val preferenceHelper by lazy { PreferenceHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)

        recyclerView = findViewById(R.id.rvContactAddTemp)
        selectedRecyclerView = findViewById(R.id.rvSelectedContacts)
        etSearch = findViewById(R.id.etSearch)
        selectedRecyclerView.visibility = View.GONE

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Enable the Up button in the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        createDatabase()
        getUsersList()

        // Check and request contacts permission if needed
        if (checkContactsPermission()) {
            onContactsPermissionGranted()
        } else {
            requestContactsPermission()
        }

        val fabToggle: FloatingActionButton = findViewById(R.id.fabToggle)
        fabToggle.setOnClickListener {
            // Handle FloatingActionButton click
            lifecycleScope.launch {
                addSelectedContactsToDatabase(selectedContacts)
                showToast("Friends added successfully!")
                finish()
            }
        }
    }

    private fun onContactsPermissionGranted() {
        loadContacts()

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterContacts(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun checkContactsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestContactsPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_CONTACTS),
            CONTACTS_PERMISSION_REQUEST
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CONTACTS_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, load contacts
                onContactsPermissionGranted()
            } else {
                // Permission denied, handle accordingly
                showToast("Contacts permission denied.")
            }
        }
    }


    private fun loadContacts() {
        // Get all contacts initially
        allContacts = getPhoneContacts()

        // Set up RecyclerView for all contacts
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        contactsAdapter = ContactsAdapter(allContacts, ::onContactSelected)
        recyclerView.adapter = contactsAdapter

        // Set up RecyclerView for selected contacts
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            val selectedLayoutManager = GridLayoutManager(this,5)
            selectedRecyclerView.layoutManager = selectedLayoutManager
            selectedContactsAdapter = SelectedContactsAdapter(selectedContacts, ::onRemoveContact)
            selectedRecyclerView.adapter = selectedContactsAdapter
        }else {
            val selectedLayoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            selectedRecyclerView.layoutManager = selectedLayoutManager
            selectedContactsAdapter = SelectedContactsAdapter(selectedContacts, ::onRemoveContact)
            selectedRecyclerView.adapter = selectedContactsAdapter
        }
    }

    @SuppressLint("Range")
    private fun getPhoneContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val contentResolver: ContentResolver = contentResolver
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                contacts.add(Contact(name, phoneNumber))
            } while (cursor.moveToNext())

            cursor.close()
        }

        return contacts
    }

    private fun filterContacts(query: String) {
        val filteredContacts = allContacts.filter {
            it.name.contains(query, ignoreCase = true) || it.phoneNumber.contains(query)
        }

        contactsAdapter.updateData(filteredContacts)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        val names: ArrayList<String> = ArrayList()
        val phoneNumber: ArrayList<String> = ArrayList()
        for (i in selectedContacts) {
            names.add(i.name)
            phoneNumber.add(i.phoneNumber)
        }
        outState.putStringArrayList("names", names)
        outState.putStringArrayList("phoneNumbers",phoneNumber)
        super.onSaveInstanceState(outState)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Restore selected contacts after an orientation change
        selectedContacts.clear()
        val names = savedInstanceState.getStringArrayList("names")
        val phoneNumbers = savedInstanceState.getStringArrayList("phoneNumbers")
        val size = names!!.size
        Log.e("size", size.toString())
        for (index in 0 until size) {
                val name = names[index]
                val phoneNumber = phoneNumbers!![index]
                selectedContacts.add(Contact(name, phoneNumber))
                Log.e("storeComplete",index.toString())
            selectedContactsAdapter.notifyDataSetChanged()
            updateSelectedContactsVisibility()
        }
    }

    private fun onContactSelected(contact: Contact) {
        // Check if the contact is not already in the selectedContacts list
        if (!selectedContacts.contains(contact)) {
            // Add the selected contact to the list
            selectedContacts.add(contact)

            // Update the selected contacts RecyclerView
            selectedContactsAdapter.notifyDataSetChanged()

            // Update visibility
            updateSelectedContactsVisibility()
        } else {
            // Optionally, you can show a toast or handle this case in another way
            showToast("Contact already selected!")
        }
    }

    private fun onRemoveContact(contact: Contact) {
        // Remove the selected contact from the list
        selectedContacts.remove(contact)

        // Update the selected contacts RecyclerView
        selectedContactsAdapter.notifyDataSetChanged()

        // Update visibility
        updateSelectedContactsVisibility()
    }

    private fun updateSelectedContactsVisibility() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvSelectedContacts)
        recyclerView.visibility = if (selectedContacts.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button click
                onBackPressed()
                return true
            }
            // Handle other menu items if needed
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private suspend fun addSelectedContactsToDatabase(selectedContacts: MutableList<Contact>) {
        // Get the current user
        val currentUser =
            usersList.firstOrNull { it.user_id == preferenceHelper.readLongFromPreference(SplitwiseApplication.PREF_USER_ID) }

        for (contact in selectedContacts) {
            // Check if the contact already exists in the users_table
            val existingUser = usersList.firstOrNull { it.phone == contact.phoneNumber }

            if (existingUser == null) {
                // Contact does not exist, so add them to the users_table
                val userEntity = UserEntity(
                    name = contact.name,
                    phone = contact.phoneNumber,
                    email = "", // Provide default or empty values
                    password = "", // Provide default or empty values
                    gender = "", // Provide default or empty values
                    owe = "0.0", // Provide default or empty values
                    owes = "0.0" // Provide default or empty values
                )
                // Insert the new user into the users_table
                userViewModel.addUser(userEntity)
            }

            if (currentUser != null) {
                delay(100)
                val existingUser = userViewModel.getUserIdByName(contact.name)
                Log.d("Friends", "Current:$currentUser")
                Log.d("Friends", "Existing:$existingUser")
                if (existingUser != null) {
                    getFriendList(currentUser.user_id) { fetchedFriendList ->
                        Log.d("FriendsList", "$fetchedFriendList")
                        val existingFriend =
                            fetchedFriendList.firstOrNull { it.friendUserId == existingUser }

                        if (existingFriend == null) {
                            // Contact is not a friend, so add them as a friend of the current user
                            val friendEntity = FriendEntity(
                                userId = currentUser.user_id,
                                friendUserId = existingUser,
                                owe = 0.0,
                                owes = 0.0,
                                totalDue = 0.0,
                                name = contact.name
                            )
                            // Insert the new friend into the friends_table
                            friendViewModel.addFriend(friendEntity)
                        }
                    }
                }
            }
        }
    }

    private fun createDatabase() {
        val appClass = application as SplitwiseApplication

        val friendRepository = appClass.friendRepository
        val friendViewModelFactory = FriendViewModelFactory(friendRepository)

        val userRepository = appClass.userRepository
        val userViewModelFactory = UserViewModelFactory(userRepository)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        friendViewModel = ViewModelProvider(this, friendViewModelFactory)
            .get(FriendViewModel::class.java)
    }

    private fun getUsersList() {
        userViewModel.getUserList().observe(this, Observer {
            usersList.clear()
            usersList.addAll(it)
        })
    }

    private suspend fun getFriendList(currentUserId: Long, callback: (List<FriendEntity>) -> Unit) {
        friendViewModel.getFriendsList(currentUserId).observe(this) { friends ->
            friendList.clear()
            friends?.let {
                friendList.addAll(it)
                callback(friendList)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}

data class Contact(val name: String, val phoneNumber: String)
