package com.example.vibescan

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper

class DB(context: Context, string: String = "SentimentAnalysis", cursorFactory: CursorFactory? = null, version: Int = 1) : SQLiteOpenHelper(context, string, cursorFactory, version) {

    private val tableName : String = "DeepMode"
    private val sentiment : String = "Sentiment"
    private val sentence : String = "Sentence"
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $tableName ($sentiment text, $sentence text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
//        db?.execSQL("drop table if exists $tableName")
//        onCreate(db)
    }

    fun onInsert(sentiment : String, sentence : String){
        val contentValue : ContentValues = ContentValues()
        contentValue.put(this.sentiment,sentiment)
        contentValue.put(this.sentence,sentence)

        val db : SQLiteDatabase = this.writableDatabase
        db.insert(tableName,null,contentValue)

        contentValue.clear();
    }

    fun onFetch() : Cursor{
        val db : SQLiteDatabase = this.readableDatabase
        val cursor : Cursor = db.rawQuery("select * from $tableName",null)
        return cursor
    }

    fun onDelete(){
        val db : SQLiteDatabase = this.writableDatabase
        db.delete(tableName,null,null)
//        db.execSQL("delete from $tableName")
        db.close()
    }
}