package com.example.notepad.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.notepad.RcMainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyDbManager(context: Context) {
    private val myDbHelper = MyDbHelper(context) // initialise the database
    private var db: SQLiteDatabase ?= null

    fun openDb(){   // open the database
        db = myDbHelper.writableDatabase
    }

    suspend fun insertToDb(title: String, content: String,date:Long,time:Int,dateAndTime:Long) = withContext(Dispatchers.IO){   // the model of writing in the database
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
            put(MyDbNameClass.COLUMN_NAME_DATE, date)
            put(MyDbNameClass.COLUMN_NAME_TIME, time)
            put(MyDbNameClass.COLUMN_NAME_DATE_AND_TIME,dateAndTime)
        }
        db?.insert(MyDbNameClass.TABLE_NAME, null, values) // insert in the database
    }

    suspend fun updateItem(title: String, content: String,id:Int) = withContext(Dispatchers.IO){  // update the bd
        val selection = BaseColumns._ID + "=$id"
        val values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_TITLE, title)
            put(MyDbNameClass.COLUMN_NAME_CONTENT, content)
        }
        db?.update(MyDbNameClass.TABLE_NAME,values,selection,null) // insert in the database
    }

    @SuppressLint("Range")
    suspend fun readDbData(search:String): ArrayList<RcMainModel> = withContext(Dispatchers.IO){  // get table name from the database
        val dataList = ArrayList<RcMainModel>()
        val selection = "${MyDbNameClass.COLUMN_NAME_DATE} like ?"
        val cursor = db?.query(MyDbNameClass.TABLE_NAME, null, selection, arrayOf("%$search%"),
            null, null, "dateAndTime")

            while (cursor?.moveToNext()!!){
                val dataTitle = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TITLE))
                val dataDesc = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_CONTENT))
                val dataDate = cursor.getLong(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_DATE))
                val dataTime = cursor.getInt(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_TIME))
                val dataId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))

                val item = RcMainModel()
                item.title = dataTitle
                item.desc = dataDesc
                item.date = dataDate
                item.time = dataTime
                item.id = dataId
                dataList.add(item)
            }
        cursor.close()
        return@withContext dataList
    }

    @SuppressLint("Range")
    fun readDbDataTime(search:String): ArrayList<Long>{  // get table name from the database
        val dataList = ArrayList<Long>()
        val selection = "${MyDbNameClass.COLUMN_NAME_DATE} like ?"
        val cursor = db?.query(
            MyDbNameClass.TABLE_NAME, null, selection, arrayOf("%$search%"),
            null, null, "dateAndTime"
        )

        while (cursor?.moveToNext()!!) {
            val dataAndTime = cursor.getLong(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_DATE_AND_TIME))
            dataList.add(dataAndTime)
        }
        cursor.close()
        return dataList
    }

    fun removeItemFromDb(id:String){   // the model of delete column from the database
        val selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.TABLE_NAME, selection, null)
    }

    fun closeDb(){
        myDbHelper.close()
    }
}