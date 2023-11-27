package com.example.billbuddy.jing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.billbuddy.R

class FragmentB : Fragment() {
    private var mTvData: TextView? = null
    private var mParam1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            mParam1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_b, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Access views and set data
        // mTvData = view.findViewById(R.id.tvData)
        // mTvData.text = mParam1
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private val TAG = FragmentB::class.java.simpleName

        fun newInstance(param1: String?): FragmentB {
            val fragment = FragmentB()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }
}
