package com.example.billbuddy.menubartrail.ui.home

// ... (other imports)
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billbuddy.databinding.FragmentFriendDetailBinding
import com.example.billbuddy.vinay.database.SplitwiseDatabase
import com.example.billbuddy.vinay.viewmodels.FriendDetailViewModel

class FriendDetailFragment : Fragment() {
    private var _binding: FragmentFriendDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var friendDetailViewModel: FriendDetailViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFriendDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val friendId = arguments?.getLong("FRIEND_ID") ?: -1
        val transactionDAO = SplitwiseDatabase.getDatabase(requireContext()).getMyTransactionEntries()
        val viewModelFactory = FriendDetailViewModelFactory(transactionDAO, friendId)
        friendDetailViewModel = ViewModelProvider(this, viewModelFactory)[FriendDetailViewModel::class.java]

        binding.recyclerViewTransactions.layoutManager = LinearLayoutManager(context)
        val adapter = TransactionAdapter(emptyList())
        binding.recyclerViewTransactions.adapter = adapter

        friendDetailViewModel.transactionsList.observe(viewLifecycleOwner) { transactions ->
            adapter.updateData(transactions)
        }

        setupActions()
    }

    private fun setupActions() {
        binding.buttonSettleUp.setOnClickListener {
            // Add action for the button
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
