import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.billbuddy.databinding.FragmentTransactionDetailBinding
import com.example.billbuddy.vinay.database.SplitwiseDatabase
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.database.transactions.TransactionEntity
import com.example.billbuddy.vinay.database.users.UserDAO
import kotlinx.coroutines.*

class TransactionDetailFragment : Fragment() {
    private var _binding: FragmentTransactionDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var transaction: TransactionEntity
    private lateinit var userDao: UserDAO
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    companion object {
        private const val TRANSACTION_KEY = "transaction"
        fun newInstance(transaction: TransactionEntity): TransactionDetailFragment {
            val fragment = TransactionDetailFragment()
            val args = Bundle()
            args.putSerializable(TRANSACTION_KEY, transaction)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transaction = arguments?.getSerializable(TRANSACTION_KEY) as TransactionEntity
        userDao = SplitwiseDatabase.getDatabase(requireContext()).getMyUserEntries()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewTransactionId.text = "Transaction Description: ${transaction.description}"
        binding.textViewAmount.text = "Amount: ${transaction.totalAmount}"
        binding.textViewDate.text = "Date: ${transaction.transactionDateTime}"
        binding.textViewComments.text = "Comments: ${transaction.comments}"
        binding.textViewNotes.text = "Notes: ${transaction.notes}"

        transaction.receiptImage?.let {
            Glide.with(this).load(it).into(binding.imageViewReceipt)
        }
        binding.imageViewReceipt.setOnTouchListener(CustomTouchListener(requireContext()))
        binding.fabAddComment.setOnClickListener {
            Log.d("TransactionDetail", "Add comment button clicked")
            showAddCommentDialog()
        }
        fetchAdditionalDetails(transaction)
    }
    private fun getCurrentUserId(): Long {
        val sharedPreferences = requireActivity().getSharedPreferences("YourPreferenceName", Context.MODE_PRIVATE)
        return sharedPreferences.getLong("currentUserId", -1) // -1 or any default value indicating no ID was found
    }
    private fun showAddCommentDialog() {
        coroutineScope.launch {
            val preferenceHelper = PreferenceHelper(requireContext())
            val email = preferenceHelper.readStringFromPreference("USER_EMAIL")

            val userId = withContext(Dispatchers.IO) {
                userDao.getUserIdByEmail(email)
            }

            userId?.let {
                val userName = withContext(Dispatchers.IO) {
                    userDao.getUserNameById(it)
                }

                val commentEditText = EditText(context).apply {
                    hint = "Enter comment"
                }

                AlertDialog.Builder(requireContext())
                    .setTitle("Add Comment")
                    .setView(commentEditText)
                    .setPositiveButton("Submit") { _, _ ->
                        val comment = "$userName: ${commentEditText.text.toString()}"
                        addCommentToDb(comment)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            } ?: Log.e("TransactionDetail", "User ID not found for email: $email")
        }
    }

    private fun addCommentToDb(comment: String) {
        coroutineScope.launch(Dispatchers.IO) {
            val updatedTransaction = transaction.copy(comments = "${transaction.comments}\n$comment")
            SplitwiseDatabase.getDatabase(requireContext()).getMyTransactionEntries().updateTransaction(updatedTransaction)
        }
    }

    private fun fetchAdditionalDetails(transaction: TransactionEntity) {
        coroutineScope.launch {
            val payerName = withContext(Dispatchers.IO) {
                userDao.getUserNameById(transaction.paidByUserId)
            }
            val membersDetails = withContext(Dispatchers.IO) {
                SplitwiseDatabase.getDatabase(requireContext())
                    .getMyNonGroupTransactionMemberEntries()
                    .getNonGroupTransactionMembersWithAmounts(transaction.transactionId, transaction.paidByUserId)
            }

            withContext(Dispatchers.Main) {
                binding.textViewPayer.text = "Paid by: $payerName"
                val involvedMembers = membersDetails.joinToString("\n ") { member ->
                    "${member.name} - Owe: ${member.amountOwe}"
                }
                binding.textViewGroup.text = "Involved: \n$involvedMembers"
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        coroutineScope.cancel()
    }
}