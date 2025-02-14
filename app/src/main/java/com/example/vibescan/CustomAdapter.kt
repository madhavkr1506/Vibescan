package com.example.vibescan

import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract.Colors
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.graphics.ColorUtils

class CustomAdapter(private val mainActivity: MainActivity,context : Context, private val sentimentQuestions : ArrayList<String>, private val sentimentMatrices : ArrayList<String>) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,sentimentQuestions) {

    private var lastOpenInputField : EditText? = null
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view  = convertView ?: inflater.inflate(R.layout.custom_adapter,parent,false)

        val queryView = view.findViewById<TextView>(R.id.query)
        val keywordView = view.findViewById<TextView>(R.id.keyword)

        val send =view.findViewById<ImageButton>(R.id.send)

        queryView.text = sentimentQuestions[position]
        keywordView.hint = sentimentMatrices[position]

        val inputField = view.findViewById<EditText>(R.id.inputField)

        queryView.setOnClickListener {
            lastOpenInputField?.visibility = View.GONE
            inputField.visibility = View.VISIBLE
            inputField.requestFocus()
            lastOpenInputField = inputField
        }

        send.setOnClickListener{
            val inputText = lastOpenInputField?.text.toString();
            lastOpenInputField?.isClickable = false
            lastOpenInputField?.isEnabled = false
            send.isEnabled = false
            if(inputText.isNotEmpty()){
                mainActivity.analyzeSentence(inputText)
            }
        }
        return view
    }

    override fun getCount(): Int {
        return sentimentQuestions.size
    }

}