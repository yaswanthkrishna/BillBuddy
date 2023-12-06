package com.example.billbuddy.vinayactivity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.example.billbuddy.R
import java.util.Locale

class NotesFragment : DialogFragment() {

    private val SPEECH_REQUEST_CODE = 1001

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

        toolbar.inflateMenu(R.menu.menu_voicenotes)
        val voiceInputMenuItem = toolbar.menu.findItem(R.id.action_voice_input)

        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.voicenotes)
        drawable?.let {
            it.setBounds(0, 0, 50.dpToPx(), 50.dpToPx()) // Adjust the size as needed
            voiceInputMenuItem.icon = it
        }
        // Set up click listener for voice input icon
        voiceInputMenuItem.setOnMenuItemClickListener {
            startVoiceInput()
            true
        }
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

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now")

        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE)
        } catch (e: Exception) {
            // Handle exception or show an error message
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == android.app.Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (result != null && result.isNotEmpty()) {
                val spokenText = result[0]
                val etNotes = view?.findViewById<EditText>(R.id.etNotes)
                etNotes?.append(spokenText)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the style and dimensions to make the fragment take up the entire screen
        setStyle(STYLE_NORMAL, android.R.style.Theme_NoTitleBar_Fullscreen)
    }

    private fun Int.dpToPx(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}
