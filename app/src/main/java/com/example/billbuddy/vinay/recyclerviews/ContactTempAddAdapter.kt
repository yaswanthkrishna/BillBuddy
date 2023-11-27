package com.example.billbuddy.vinay.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.billbuddy.R
import com.example.billbuddy.vinay.recyclerviews.ContactTempModel
import com.example.billbuddy.databinding.TempContactItemLayoutBinding

class ContactTempAddAdapter(
    private val contactList: List<ContactTempModel>,
    private val contactCommunicator: ContactCommunicator
) : RecyclerView.Adapter<ContactTempViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactTempViewHolder {
        val binding = TempContactItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ContactTempViewHolder(binding, contactCommunicator)
    }

    override fun getItemCount(): Int = contactList.size

    override fun onBindViewHolder(holder: ContactTempViewHolder, position: Int) {
        val tempModel = contactList[position]
        holder.bind(tempModel)
    }

    fun getNamesList(): List<String> {
        return contactList.map { it.name.toString() }
    }
}

class ContactTempViewHolder(
    private val binding: TempContactItemLayoutBinding,
    private val contactCommunicator: ContactCommunicator
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(tempModel: ContactTempModel) {
        binding.tvContactNameTemp.text = tempModel.name
        binding.imgCv.setOnClickListener {
            contactCommunicator.onContactDelete(tempModel)
        }
    }
}