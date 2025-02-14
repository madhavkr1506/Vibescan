package com.example.vibescan

import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PageDesign : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_design)

        val PositiveSentenceList: MutableList<String> = mutableListOf();
        val PositiveSentimentList: MutableList<String> = mutableListOf();
        val NegativeSentenceList: MutableList<String> = mutableListOf();
        val NegativeSentimentList: MutableList<String> = mutableListOf();
        val NeutralSentenceList: MutableList<String> = mutableListOf();
        val NeutralSentimentList: MutableList<String> = mutableListOf();

        val dbHelper = DB(this)

        CoroutineScope(Dispatchers.IO).launch {
            val cursor: Cursor = dbHelper.onFetch()
            while (cursor.moveToNext()) {
                val sentimentItem = cursor.getString(0)
                val sentenceItem = cursor.getString(1)

                Log.d("Database", "Sentiment: $sentimentItem, Sentence: $sentenceItem")

                if (sentimentItem.equals("Positive", true)) {
                    PositiveSentimentList.add(sentimentItem)
                    PositiveSentenceList.add(sentenceItem)
                } else if (sentimentItem.equals("Negative", true)) {
                    NegativeSentimentList.add(sentimentItem)
                    NegativeSentenceList.add(sentenceItem)
                } else if (sentimentItem.equals("Neutral", true)) {
                    NeutralSentimentList.add(sentimentItem)
                    NeutralSentenceList.add(sentenceItem)
                }
            }

            val positiveList = findViewById<ListView>(R.id.positiveList)
            val positiveAdapter =
                PositiveListCustomAdapter( this@PageDesign, PositiveSentimentList, PositiveSentenceList)

            withContext(Dispatchers.Main){
                positiveList.adapter = positiveAdapter
            }

            val negativeList = findViewById<ListView>(R.id.negativeList)
            val negativeAdapter =
                NegativeListCustomAdapter( this@PageDesign, NegativeSentimentList, NegativeSentenceList)

            withContext(Dispatchers.Main){
                negativeList.adapter = negativeAdapter
            }

            val neutralList = findViewById<ListView>(R.id.neutralList)
            val neutralAdapter =
                NeutralListCustomAdapter( this@PageDesign, NeutralSentimentList, NeutralSentenceList)

            withContext(Dispatchers.Main){
                neutralList.adapter = neutralAdapter
            }

        }
    }
}