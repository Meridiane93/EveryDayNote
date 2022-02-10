package com.example.notepad.dateAndTime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.notepad.Constants
import com.example.notepad.R
import com.example.notepad.databinding.ActivityCalendarMainBinding
import java.util.*

class CalendarMainActivity : AppCompatActivity() {

    private lateinit var bindingActivityCalendarMain: ActivityCalendarMainBinding
    private val nowDate = Calendar.getInstance()
    private val converterDate =  ConverterDate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingActivityCalendarMain = ActivityCalendarMainBinding.inflate(layoutInflater)
        setContentView(bindingActivityCalendarMain.root)

        val cal = Calendar.getInstance()

        bindingActivityCalendarMain.apply {
            calendarMain.minDate = nowDate.time.time
            calendarMain.maxDate = nowDate.time.time + 8640000000 // range to 100 days

            txNumberMainActivityData.text =
                getString(R.string.numberTextDate, cal.get(Calendar.DAY_OF_MONTH)) // now date text

            val message = intent.getStringExtra(Constants.DATE_STRING)
            calendarMain.date = converterDate.convertString(message.toString())

            calendarMain.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val dateText = "$dayOfMonth/${month + 1}/$year"
                val intentDate = Intent()
                intentDate.putExtra(Constants.DATE_LONG, converterDate.convertString(dateText))
                intentDate.putExtra(Constants.DATE_STRING_CALENDAR, dateText)
                setResult(RESULT_OK, intentDate)
                finish()
            }
        }
    }

    fun allNote(@Suppress("UNUSED_PARAMETER")view: View) {
        val intentDate = Intent()
        intentDate.putExtra(Constants.DATE_LONG, 0L)
        intentDate.putExtra(Constants.DATE_STRING_CALENDAR, getString(R.string.all_note))
        setResult(RESULT_OK, intentDate)
        finish()
    }
}