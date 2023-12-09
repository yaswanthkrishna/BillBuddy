package com.example.billbuddy.vinayactivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R
import com.example.billbuddy.vinay.database.friend_non_group.FriendEntity
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.database.users.UserEntity
import com.example.billbuddy.vinay.recyclerviews.ContactTempModel
import com.example.billbuddy.vinay.repositories.FriendRepository
import com.example.billbuddy.vinay.repositories.UserRepository
import com.example.billbuddy.vinay.viewmodels.FriendViewModel
import com.example.billbuddy.vinay.viewmodels.FriendViewModelFactory
import com.example.billbuddy.vinay.viewmodels.UserViewModel
import com.example.billbuddy.vinay.viewmodels.UserViewModelFactory
import com.example.billbuddy.vinay.views.SplitwiseApplication

class SettleUpListFragment : Fragment(), FriendsSettlementAdapter.OnFriendItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var friendsAdapter: FriendsSettlementAdapter
    private lateinit var userRepository: UserRepository
    private lateinit var friendRepository: FriendRepository
    private var friendList = mutableListOf<FriendEntity>()
    private var usersList = mutableListOf<UserEntity>()
    private lateinit var friendViewModel: FriendViewModel
    private lateinit var userViewModel: UserViewModel
    private val preferenceHelper by lazy { PreferenceHelper(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        createDatabase()
        getUsersList()
        return inflater.inflate(R.layout.fragment_settle_up_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val friends = mutableListOf<String>()
        val amountsOwed = mutableListOf<String>()
        val friendUserID = mutableListOf<Long>()

        getFriendList(preferenceHelper.readLongFromPreference(SplitwiseApplication.PREF_USER_ID)) { friendList ->
            if (friendList.isEmpty()) {
                // Show the TextView when friendList is empty
                view.findViewById<TextView>(R.id.textNoData).visibility = View.VISIBLE
            } else {
                // Hide the TextView when friendList is not empty
                view.findViewById<TextView>(R.id.textNoData).visibility = View.GONE

                for (friend in friendList) {
                    if (friend.totalDue < 0) {
                        friends.add(friend.name)
                        amountsOwed.add(Math.abs(friend.totalDue).toString())
                        friendUserID.add(friend.friendUserId)
                    }
                }

                Log.d("SettleupList", "$usersList")
                Log.d("SettleupList", "$friendList")
                Log.d("SettleupList", "$friends")
                Log.d("SettleupList", "$amountsOwed")

                // RecyclerView setup
                recyclerView = view.findViewById(R.id.recyclerViewFriends)
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                friendsAdapter = FriendsSettlementAdapter(friends, amountsOwed, friendUserID, this)
                recyclerView.adapter = friendsAdapter

                // Notify the adapter that the data set has changed
                friendsAdapter.notifyDataSetChanged()
            }
        }
    }


    // Handle Friend Item Click
    override fun onFriendItemClick(position: Int) {
        val selectedFriend = friendsAdapter.getFriendName(position)
        val selectedAmount = friendsAdapter.getAmountOwed(position)
        val selectedFriendUserId = friendsAdapter.getFriendUserId(position)

        Log.d("CustomeSettlementList","$selectedAmount")
        // Create a new fragment for custom settlement
        val customSettlementFragment = CustomSettlementFragment.newInstance(selectedFriend, selectedAmount,selectedFriendUserId)

        // Show the fragment
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer_settleup, customSettlementFragment)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    private fun createDatabase() {
        val appClass = requireActivity().application as SplitwiseApplication

        userRepository = appClass.userRepository
        val userViewModelFactory = UserViewModelFactory(userRepository)

        friendRepository = appClass.friendRepository
        val friendViewModelFactory = FriendViewModelFactory(friendRepository)

        userViewModel = ViewModelProvider(this, userViewModelFactory)
            .get(UserViewModel::class.java)

        friendViewModel = ViewModelProvider(this, friendViewModelFactory)
            .get(FriendViewModel::class.java)
    }

    private fun getUsersList() {
        userViewModel.getUserList().observe(viewLifecycleOwner) { users ->
            usersList.clear()
            usersList.addAll(users)
        }
    }

    private fun getFriendList(currentUserId: Long, callback: (List<FriendEntity>) -> Unit) {
        friendViewModel.getFriendsList(currentUserId).observe(viewLifecycleOwner, Observer { friends ->
            friends?.let {
                friendList.clear()
                friendList.addAll(it)

                // Invoke the callback with the updated friend list
                callback(friendList)
            }
        })
    }
}
