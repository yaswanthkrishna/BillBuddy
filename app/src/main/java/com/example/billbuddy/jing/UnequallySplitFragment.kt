package com.example.billbuddy.jing

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R
import com.example.billbuddy.vinay.recyclerviews.ContactTempModel

class UnequallySplitFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var selectedPersons: List<ContactTempModel>
    private lateinit var amountTextView: TextView  // Declare amountTextView here
    private lateinit var amountLeftTextView: TextView
    private var amount:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_unequally_split, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        // Enable the back button
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Split Unequally"

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        val saveButton = AppCompatButton(requireContext())
        saveButton.text = "Save"
        val params = Toolbar.LayoutParams(
            Toolbar.LayoutParams.WRAP_CONTENT,
            Toolbar.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.END
        saveButton.layoutParams = params
        saveButton.setOnClickListener {
            // Handle the save action
            saveExpense()
        }

        // Add the button to the toolbar
        toolbar.addView(saveButton)

        amountTextView = view.findViewById(R.id.amountTextView)
        amountLeftTextView = view.findViewById(R.id.amountLeftTextView)

        arguments?.let {
            selectedPersons = it.getParcelableArrayList<ContactTempModel>("selectedPersons") ?: emptyList()
            amount = it.getInt("amount", 0)
            Log.d("AmountFragment","$amount")

            amountTextView.text = "Of $amount"
            updateAmountLeft()
        }
        return view
    }


    private fun saveExpense() {
        val adapter = recyclerView.adapter as? PersonAdapter
        val amounts = adapter?.getAmountsList() ?: emptyMap()
        val totalAmountEntered = amounts.values.sum()

        // Calculate the amount left
        val amountLeft = amount - totalAmountEntered

        // Check if the amount left is greater than zero
        if (amountLeft > 0) {
            // Display an error message
            amountLeftTextView.text = "Amount Left: $amountLeft."
            amountLeftTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.harley_davidson_orrange))
            return  // Exit the function without saving
        }
        // Send the amounts back to the AddExpenseActivity
        (activity as? OnAmountsSavedListener)?.onAmountsSaved(amounts)

        // Pop the fragment from the back stack
        fragmentManager?.popBackStack()
    }

    // Define an interface for communication with AddExpenseActivity
    interface OnAmountsSavedListener {
        fun onAmountsSaved(amounts: Map<String, Int>)
    }

    private fun updateAmountLeft() {
        // Calculate the total amount entered for each person
        val adapter = recyclerView.adapter as? PersonAdapter
        val amounts = adapter?.getAmountsList() ?: emptyMap()
        val totalAmountEntered = amounts.values.sum()

        // Calculate and update the amountLeftTextView
        val amountLeft = amount - totalAmountEntered

        // Display an error if the total amount entered is invalid
        if (totalAmountEntered > amount) {
            amountLeftTextView.text = "Amount Entered exceeds Total amount"
            amountLeftTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.harley_davidson_orrange))
        } else {
            amountLeftTextView.text = "Amount Left: $amountLeft"
            amountLeftTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Assuming you have a LinearLayoutManager, adjust if you use a different layout manager
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        // Use the selectedPersons directly
        val adapter = PersonAdapter(selectedPersons, this::updateAmountLeft)
        recyclerView.adapter = adapter

    }


    class PersonAdapter(private val personList: List<ContactTempModel>,
                        private val updateAmountLeft: () -> Unit) :
        RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

        private val amountEditTextList = mutableListOf<EditText>()

        class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView: TextView = itemView.findViewById(R.id.personNameTextView)
            val amountEditText: EditText = itemView.findViewById(R.id.amountEditText)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_person, parent, false)
            val holder = PersonViewHolder(view)
            amountEditTextList.add(holder.amountEditText)
            return holder
        }

        override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
            val person = personList[position]
            holder.nameTextView.text = person.name
            // Set any additional properties or listeners for the amount EditText here

            holder.amountEditText.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {
                    updateAmountLeft.invoke()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No implementation needed
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // No implementation needed
                }
            })
        }

        override fun getItemCount(): Int {
            return personList.size
        }

        fun getAmountsList(): Map<String, Int> {
            val amountsMap = mutableMapOf<String, Int>()
            for (i in 0 until itemCount) {
                val person = personList[i]
                val amountStr = amountEditTextList[i].text.toString()
                val amount = if (amountStr.isNotEmpty()) amountStr.toInt() else 0
                amountsMap[person.name ?: ""] = amount
            }
            return amountsMap
        }
    }

}