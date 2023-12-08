package com.example.billbuddy.vinayactivity

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.pm.ActivityInfo
import android.content.pm.ConfigurationInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R
import com.example.billbuddy.vinay.database.friend_non_group.FriendEntity
import com.example.billbuddy.vinay.database.groups.GroupListEntity
import com.example.billbuddy.vinay.database.groups.GroupMemberEntity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.database.users.UserEntity
import com.example.billbuddy.vinay.viewmodels.FriendViewModel
import com.example.billbuddy.vinay.viewmodels.GroupListViewModel
import com.example.billbuddy.vinay.viewmodels.GroupListViewModelFactory
import com.example.billbuddy.vinay.viewmodels.GroupMemberViewModel
import com.example.billbuddy.vinay.viewmodels.GroupMemberViewModelFactory
import com.example.billbuddy.vinay.viewmodels.UserViewModel
import com.example.billbuddy.vinay.viewmodels.UserViewModelFactory
import com.example.billbuddy.vinay.views.SplitwiseApplication
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddGroupMembers : Fragment() {

    private val CONTACTS_PERMISSION_REQUEST = 1
    private lateinit var recyclerView: RecyclerView
    private lateinit var selectedRecyclerView: RecyclerView
    private lateinit var contactsAdapter: ContactsAdapter
    private lateinit var selectedContactsAdapter: SelectedContactsAdapter
    private lateinit var etSearch: EditText
    private var allContacts: List<Contact> = emptyList()
    private var selectedContacts: MutableList<Contact> = mutableListOf()
    private lateinit var friendViewModel: FriendViewModel
    private var usersList = mutableListOf<UserEntity>()
    private var friendList = mutableListOf<FriendEntity>()
    private val preferenceHelper by lazy { PreferenceHelper(requireContext()) }
    private lateinit var groupListViewModel: GroupListViewModel
    private lateinit var groupMemberViewModel: GroupMemberViewModel
    private lateinit var userViewModel: UserViewModel
    private var groupName: String? = null
    private var groupCategory: String? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_group_members, container, false)

        groupName = arguments?.getString("groupName")
        groupCategory = arguments?.getString("groupCategory")

        recyclerView = view.findViewById(R.id.rvContactAddTemp)
        selectedRecyclerView = view.findViewById(R.id.rvSelectedContacts)
        etSearch = view.findViewById(R.id.etSearch)
        selectedRecyclerView.visibility = View.GONE

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)

        // Set the Toolbar as the action bar for the activity
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        // Enable the Up button in the toolbar
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        createDatabase()
        getUsersList()

        // Check and request contacts permission if needed
        if (checkContactsPermission()) {
            onContactsPermissionGranted()
        } else {
            requestContactsPermission()
        }

        val fabToggle: FloatingActionButton = view.findViewById(R.id.fabToggle)
        fabToggle.setOnClickListener {
            // Handle FloatingActionButton click
            lifecycleScope.launch {
                addSelectedContactsToGroupDatabase(selectedContacts)
                showToast("Group Created successfully!")

                // Close the activity along with the fragment
                requireActivity().finish()
            }
        }

        return view
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
            requireContext(),
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestContactsPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
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
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        contactsAdapter = ContactsAdapter(allContacts, ::onContactSelected)
        recyclerView.adapter = contactsAdapter

        // Set up RecyclerView for selected contacts
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            val selectedLayoutManager = GridLayoutManager(requireContext(),5)
            selectedRecyclerView.layoutManager = selectedLayoutManager
            selectedContactsAdapter = SelectedContactsAdapter(selectedContacts, ::onRemoveContact)
            selectedRecyclerView.adapter = selectedContactsAdapter
        }else {
            val selectedLayoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            selectedRecyclerView.layoutManager = selectedLayoutManager
            selectedContactsAdapter = SelectedContactsAdapter(selectedContacts, ::onRemoveContact)
            selectedRecyclerView.adapter = selectedContactsAdapter
        }
    }

    @SuppressLint("Range")
    private fun getPhoneContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val contentResolver: ContentResolver = requireActivity().contentResolver
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
        val recyclerView = view?.findViewById<RecyclerView>(R.id.rvSelectedContacts)
        recyclerView?.visibility = if (selectedContacts.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button click
                requireActivity().onBackPressed()
                return true
            }
            // Handle other menu items if needed
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private suspend fun addSelectedContactsToGroupDatabase(selectedContacts: MutableList<Contact>) {
        // Get the current user
        val currentUser =
            usersList.firstOrNull { it.user_id == preferenceHelper.readLongFromPreference(SplitwiseApplication.PREF_USER_ID) }

        val groupEntity = GroupListEntity(
            groupName = groupName?:"",
            groupCategory = groupCategory?:""
        )
        groupListViewModel.addGroup(groupEntity)

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
                val existingGroup = groupName?.let { groupListViewModel.getUserIdByName(groupName!!) }
                val existingUser = userViewModel.getUserIdByName(contact.name)
                Log.d("Groups", "Current:$currentUser")
                Log.d("Groups", "Existing:$existingUser")
                if (existingGroup != null) {

                            val groupMemberEntity =
                                existingUser?.let {
                                    GroupMemberEntity(
                                        groupId = existingGroup,
                                        userId = it,
                                        userOwe = 0.0,
                                        groupOwes = 0.0,
                                        totalDue = 0.0
                                    )
                                }
                            // Insert the groupMember into the groupList_table
                    if (groupMemberEntity != null) {
                        groupMemberViewModel.addGroupMember(groupMemberEntity)
                    }
                }
            }
        }
    }

    private fun getUsersList() {
        userViewModel.getUserList().observe(viewLifecycleOwner) { users ->
            usersList.clear()
            usersList.addAll(users)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun createDatabase() {
        val appClass = requireActivity().application as SplitwiseApplication

        val userRepository = appClass.userRepository
        val userViewModelFactory = UserViewModelFactory(userRepository)

        val groupListRepository = appClass.groupListRepository
        val groupListViewModelFactory = GroupListViewModelFactory(groupListRepository)

        val groupMemberRepository = appClass.groupMemberRepository
        val groupMemberViewModelFactory = GroupMemberViewModelFactory(groupMemberRepository)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        groupListViewModel = ViewModelProvider(this, groupListViewModelFactory)
            .get(GroupListViewModel::class.java)

        groupMemberViewModel = ViewModelProvider(this, groupMemberViewModelFactory)
            .get(GroupMemberViewModel::class.java)
    }
}
