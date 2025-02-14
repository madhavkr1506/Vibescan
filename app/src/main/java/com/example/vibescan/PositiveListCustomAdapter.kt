package com.example.vibescan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class PositiveListCustomAdapter(context: Context, private val PositiveSentimentList : MutableList<String>,private val PositiveSentenceList : MutableList<String>) : ArrayAdapter<String>(context,android.R.layout.simple_list_item_1, PositiveSentenceList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view  = convertView ?: inflater.inflate(R.layout.positive_list_custom_adapter,parent, false)
        val sentiment = view.findViewById<TextView>(R.id.sentiment)
        val sentence = view.findViewById<TextView>(R.id.sentence)
        sentiment.text = PositiveSentimentList[position]
        sentence.text = PositiveSentenceList[position]
        return view
    }

    override fun getCount(): Int {
        return PositiveSentenceList.size
    }
}