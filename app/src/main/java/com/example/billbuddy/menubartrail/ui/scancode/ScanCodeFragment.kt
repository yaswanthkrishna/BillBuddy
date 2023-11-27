package com.example.billbuddy.menubartrail.ui.scancode

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.billbuddy.R

class ScanCodeFragment : Fragment() {

    companion object {
        fun newInstance() = ScanCodeFragment()
    }

    private lateinit var viewModel: ScanCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scan_code, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ScanCodeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}