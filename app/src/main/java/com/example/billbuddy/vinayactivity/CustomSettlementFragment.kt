package com.example.billbuddy.vinayactivity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.billbuddy.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size

import androidx.compose.runtime.Composable
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.compose.LottieAnimation
import com.example.billbuddy.databinding.FragmentCustomSettlementBinding
import com.example.billbuddy.newCurrencyExchange.presentation.converter.ConverterViewModel
import com.example.billbuddy.vinay.database.sharedpreferences.PreferenceHelper
import com.example.billbuddy.vinay.database.users.UserEntity
import com.example.billbuddy.vinay.repositories.FriendRepository
import com.example.billbuddy.vinay.repositories.UserRepository
import com.example.billbuddy.vinay.viewmodels.FriendViewModel
import com.example.billbuddy.vinay.viewmodels.FriendViewModelFactory
import com.example.billbuddy.vinay.viewmodels.UserViewModel
import com.example.billbuddy.vinay.viewmodels.UserViewModelFactory
import com.example.billbuddy.vinay.views.SplitwiseApplication
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import kotlinx.coroutines.runBlocking


@OptIn(ExperimentalComposeUiApi::class)
class CustomSettlementFragment : Fragment() {

    private lateinit var binding: FragmentCustomSettlementBinding
    private val TAG = "MyTag"
    private lateinit var friendViewModel: FriendViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var userRepository: UserRepository
    private lateinit var friendRepository: FriendRepository
    private val preferenceHelper by lazy { PreferenceHelper(requireContext()) }
    private var usersList = mutableListOf<UserEntity>()
    private lateinit var selectedCurrency: String
    private val viewModelConverter: ConverterViewModel by viewModels()

    companion object {
        private const val ARG_FRIEND_NAME = "friend_name"
        private const val ARG_AMOUNT_OWED = "amount_owed"
        private const val ARG_FRIEND_USER_ID = "friend_userid"

        fun newInstance(friendName: String, amountOwed: String, friendUserId: Long): CustomSettlementFragment {
            val fragment = CustomSettlementFragment()
            val args = Bundle()
            args.putString(ARG_FRIEND_NAME, friendName)
            args.putString(ARG_AMOUNT_OWED, amountOwed)
            args.putLong(ARG_FRIEND_USER_ID, friendUserId)

            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCustomSettlementBinding.inflate(inflater, container, false)
        val view = binding.root

        createDatabase()
        getUsersList()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val friendName = arguments?.getString(ARG_FRIEND_NAME) ?: ""
        val amountOwed = arguments?.getString(ARG_AMOUNT_OWED) ?: ""
        val friendUserId = arguments?.getLong(ARG_FRIEND_USER_ID) ?: 0L


        binding.friendNameTextView.text = friendName
        val currencySpinner: Spinner = binding.root.findViewById(R.id.currencySpinner)
        Log.d("Currency","input:$amountOwed")
        binding.balanceTextView.text = amountOwed
        Log.d("currencyaaa", binding.balanceTextView.text.toString())
        val editTextSettlementAmount = binding.editTextManualAmount


        selectedCurrency = "EUR"

        currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCurrency = parent?.getItemAtPosition(position).toString()
                updateBalanceTextView(amountOwed)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        binding.paymentButtonContainer.setup(
            createOrder = CreateOrder { createOrderActions ->
                val inputText = editTextSettlementAmount.text.toString()
                val settlementAmount = inputText.toDouble()
                val order = OrderRequest(
                    intent = OrderIntent.CAPTURE,
                    appContext = AppContext(userAction = UserAction.PAY_NOW),
                    purchaseUnitList = listOf(
                        PurchaseUnit(
                            amount = Amount(currencyCode = CurrencyCode.USD, value = settlementAmount.toString())
                        )
                    )
                )
                createOrderActions.create(order)
            },
            onApprove = OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.d(TAG, "CaptureOrderResult: $captureOrderResult")
                    val inputText = editTextSettlementAmount.text.toString()
                    val settlementAmount = inputText.toDouble()
                    performSettlement(settlementAmount, friendUserId)
                    Handler().postDelayed({
                        requireActivity().supportFragmentManager.popBackStack()
                    }, 4000)
                    val composeView = ComposeView(requireContext()).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }

                    composeView.setContent {
                        SettleUpScreen()
                    }

                    binding.composeContainer.removeAllViews()
                    binding.composeContainer.addView(composeView)
                }
            },
            onCancel = OnCancel {
                Log.d(TAG, "Buyer Cancelled This Purchase")
            },
            onError = OnError { errorInfo ->
                Log.d(TAG, "Error: $errorInfo")
            }
        )

        binding.settleUpButton.setOnClickListener {

            val inputText = editTextSettlementAmount.text.toString()

            if (inputText.isNotEmpty()) {
                val settlementAmount = inputText.toDoubleOrNull()

                if (settlementAmount != null && settlementAmount > 0) {
                    // Perform the settlement and update the tables
                    performSettlement(settlementAmount, friendUserId)
                    Handler().postDelayed({
                        requireActivity().supportFragmentManager.popBackStack()
                    }, 4000)

                } else {
                    Toast.makeText(requireContext(), "Invalid amount entered", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please enter an amount", Toast.LENGTH_SHORT).show()
            }

            val composeView = ComposeView(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            composeView.setContent {
                SettleUpScreen()
            }

            binding.composeContainer.removeAllViews()
            binding.composeContainer.addView(composeView)
        }
    }

    private fun updateBalanceTextView(amountOwed: String) {

        viewModelConverter.setConvertedToCurrency(selectedCurrency,amountOwed)

        Log.d("currencyaaa1","$selectedCurrency")


        viewModelConverter.convertedValue.observe(viewLifecycleOwner) { convertedValue ->
            binding.balanceTextView.text = convertedValue
        }
    }


    /*private suspend fun convertToSelectedCurrency(): Double {
        return suspendCoroutine { continuation ->
            // Assuming viewModelConverter is an instance of your ViewModel
            viewModelConverter.convertedValue.observe(viewLifecycleOwner) { convertedValue ->
                val convertedAmount = convertedValue.toDouble()
                Log.d("ConvertedCurrency", "$convertedAmount")

                // Complete the coroutine with the converted amount
                continuation.resume(convertedAmount)
            }

            // Handle the case where the conversion takes too long
            // You may want to set a timeout and return a default value if needed
        }
    }*/
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

    private fun performSettlement(settlementAmount: Double, friendUserId: Long) {

        val userId = preferenceHelper.readLongFromPreference(SplitwiseApplication.PREF_USER_ID)

        runBlocking {
            // Update the friend table with the settled amount
            friendViewModel.updateOweAmount(userId, friendUserId, -settlementAmount)
            friendViewModel.updateTotalDue(userId, friendUserId)

            val currentUser =
                usersList.firstOrNull { it.user_id == preferenceHelper.readLongFromPreference(SplitwiseApplication.PREF_USER_ID) }

            currentUser?.let {
                it.owe = (it.owe.toDouble() - settlementAmount).toString()
                userViewModel.updateUser(it)
            }

            userViewModel.updateOweById(userId, -settlementAmount)
        }

        Toast.makeText(requireContext(), "Settlement successful", Toast.LENGTH_SHORT).show()

        // Add additional logic as needed
    }


    private fun getUsersList() {
        userViewModel.getUserList().observe(viewLifecycleOwner) { users ->
            usersList.clear()
            usersList.addAll(users)
        }
    }

}

@Composable
fun SettleUpScreen() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie))

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(composition = composition, modifier = Modifier.size(400.dp))
        // No need for the "Play Again" button
        // Add other Compose elements as needed
    }
}

