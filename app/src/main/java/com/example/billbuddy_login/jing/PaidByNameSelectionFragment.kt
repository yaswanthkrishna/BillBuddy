package com.example.billbuddy_login.jing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billbuddy_login.R
import com.example.billbuddy_login.databinding.FragmentPaidByNameSelectionBinding

class NameSelectionFragment : Fragment() {

    private var nameSelectedListener: OnNameSelectedListener? = null
    private lateinit var binding: FragmentPaidByNameSelectionBinding

    companion object {
        private const val ARG_NAMES = "arg_names"

        fun newInstance(names: List<String>): NameSelectionFragment {
            val fragment = NameSelectionFragment()
            val args = Bundle().apply {
                putStringArrayList(ARG_NAMES, ArrayList(names))
            }
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var names: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaidByNameSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        // Enable the back button
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Paid By"

        // Set up the toolbar navigation
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        // Retrieve the names from arguments
        names = requireArguments().getStringArrayList(ARG_NAMES) ?: emptyList()

        // Set up RecyclerView with names
        val adapter = NameAdapter(names) { selectedName ->
            // Notify the activity when a name is selected
            nameSelectedListener?.onNameSelected(selectedName)
        }

        binding.rvContactAddTemp.layoutManager = LinearLayoutManager(requireContext())
        binding.rvContactAddTemp.adapter = adapter
    }

    fun setOnNameSelectedListener(listener: OnNameSelectedListener) {
        nameSelectedListener = listener
    }
}

interface OnNameSelectedListener {
    fun onNameSelected(name: String)
}
