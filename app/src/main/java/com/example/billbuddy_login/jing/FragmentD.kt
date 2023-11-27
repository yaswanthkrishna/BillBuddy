package com.example.billbuddy_login.jing

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.billbuddy_login.R
import com.example.billbuddy_login.databinding.FragmentDBinding
import com.example.billbuddy_login.Dashboard
import com.example.billbuddy_login.menubartrail.MenuMainActivity

class FragmentD : Fragment() {

    private var binding: FragmentDBinding? = null
    private var mParam1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mParam1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.ivClickAnywhere?.setOnClickListener {
            val intent = Intent(activity, MenuMainActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        private const val ARG_PARAM1 = "param1"

        fun newInstance(param1: String?): FragmentD {
            val fragment = FragmentD()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }
}
