package com.example.billbuddy.vinayactivity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.example.billbuddy.R

class NotesFragment : DialogFragment() {

    interface OnNoteEnteredListener {
        fun onNoteEntered(note: String)
    }

    private lateinit var onNoteEnteredListener: OnNoteEnteredListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onNoteEnteredListener = context as OnNoteEnteredListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnNoteEnteredListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.back_arrow)
        // Replace with your back arrow icon

        // Set up back button click listener
        toolbar.setNavigationOnClickListener {
            dismiss()
        }

        val etNotes = view.findViewById<EditText>(R.id.etNotes)
        val btnSaveNotes = view.findViewById<Button>(R.id.btnSaveNotes)

        btnSaveNotes.setOnClickListener {
            // Retrieve the entered notes
            val notes = etNotes.text.toString()
            // Notify the hosting activity about the entered notes
            onNoteEnteredListener.onNoteEntered(notes)
            // Dismiss the fragment after handling the notes
            dismiss()
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the style and dimensions to make the fragment take up the entire screen
        setStyle(STYLE_NORMAL, android.R.style.Theme_NoTitleBar_Fullscreen)
    }
}
