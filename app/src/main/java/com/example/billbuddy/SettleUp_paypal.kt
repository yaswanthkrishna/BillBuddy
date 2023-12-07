package com.example.billbuddy

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.example.billbuddy.databinding.SettleUpPaypalBinding
import com.paypal.checkout.PayPalCheckout
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

class SettleUp_paypal : AppCompatActivity() {

    lateinit var binding: SettleUpPaypalBinding
    val TAG = "MyTag"

    //sandbox email: sb-30or028639080@personal.example.com
    //password: c&@WUq3W
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SettleUpPaypalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.paymentButtonContainer.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    OrderRequest(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = "10.00")
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.d(TAG, "CaptureOrderResult: $captureOrderResult")
                    Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
                }
            },
            onCancel = OnCancel {
                Log.d(TAG, "Buyer Cancelled This Purchase")
                Toast.makeText(this, "Payment Cancelled", Toast.LENGTH_SHORT).show()
            },
            onError = OnError { errorInfo ->
                Log.d(TAG, "Error: $errorInfo")
                Toast.makeText(this, "Payment Error", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}