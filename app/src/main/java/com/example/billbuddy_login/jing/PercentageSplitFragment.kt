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
import kotlin.properties.Delegates

class PercentageSplitFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var selectedPersons: List<ContactTempModel>
    private lateinit var totalAmountTextView: TextView
    private lateinit var percentageTextView: TextView
    private var totalAmount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_percentage_split, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        // Enable the back button
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Percentage Split"

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

        //totalAmountTextView = view.findViewById(R.id.totalAmountTextView)
        percentageTextView = view.findViewById(R.id.percentageTextView)

        arguments?.let {
            selectedPersons =
                it.getParcelableArrayList<ContactTempModel>("selectedPersons") ?: emptyList()
            totalAmount = it.getInt("totalAmount", 0)
            Log.d("AmountFragment", "$totalAmount")

            //totalAmountTextView.text = "Total Amount: $totalAmount"
            updatePercentage()
        }
        return view
    }

    private fun saveExpense() {
        val adapter = recyclerView.adapter as? PersonAdapter
        val percentages = adapter?.getPercentageList() ?: emptyMap()
        val totalPercentage = percentages.values.sum()

        // Check if the total percentage is equal to 100
        if (totalPercentage != 100) {
            // Display an error message
            percentageTextView.text = "Total Percentage should be 100%"
            percentageTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.harley_davidson_orrange
                )
            )
            return  // Exit the function without saving
        }

        // Send the percentages back to the AddExpenseActivity
        (activity as? OnPercentagesSavedListener)?.onPercentagesSaved(percentages)

        // Pop the fragment from the back stack
        fragmentManager?.popBackStack()
    }

    // Define an interface for communication with AddExpenseActivity
    interface OnPercentagesSavedListener {
        fun onPercentagesSaved(percentages: Map<String, Int>)
    }

    private fun updatePercentage() {
        // Calculate the total percentage entered for each person
        val adapter = recyclerView.adapter as? PersonAdapter
        val percentages = adapter?.getPercentageList() ?: emptyMap()
        val totalPercentage = percentages.values.sum()

        // Display an error if the total percentage entered is invalid
        if (totalPercentage > 100) {
            percentageTextView.text = "Total Percentage exceeds 100%"
            percentageTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.harley_davidson_orrange
                )
            )
        } else {
            percentageTextView.text = "Percentage: $totalPercentage%"
            percentageTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Assuming you have a LinearLayoutManager, adjust if you use a different layout manager
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        // Use the selectedPersons directly
        val adapter = PersonAdapter(selectedPersons, this::updatePercentage)
        recyclerView.adapter = adapter
    }

    class PersonAdapter(
        private val personList: List<ContactTempModel>,
        private val updatePercentage: () -> Unit
    ) :
        RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

        private val percentageEditTextList = mutableListOf<EditText>()

        class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val personNameTextViewPercentage: TextView? = itemView.findViewById(R.id.personNameTextViewPercentage)
            val percentageEditText: EditText? = itemView.findViewById(R.id.percentageEditText)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_person_percentage, parent, false)
            val holder = PersonViewHolder(view)
            holder.percentageEditText?.let { percentageEditTextList.add(it) }
            return holder
        }

        override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
            val person = personList[position]
            holder.personNameTextViewPercentage?.text = person.name
            // Set any additional properties or listeners for the percentage EditText here

            holder.percentageEditText?.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {
                    updatePercentage.invoke()
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

        fun getPercentageList(): Map<String, Int> {
            val percentagesMap = mutableMapOf<String, Int>()
            for (i in 0 until itemCount) {
                val person = personList[i]
                val percentageStr = percentageEditTextList[i].text.toString()
                val percentage = if (percentageStr.isNotEmpty()) percentageStr.toInt() else 0
                percentagesMap[person.name ?: ""] = percentage
            }
            return percentagesMap
        }
    }
}
