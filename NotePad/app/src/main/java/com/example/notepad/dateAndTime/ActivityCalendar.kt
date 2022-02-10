package com.example.notepad.dateAndTime

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad.*
import com.example.notepad.activity.EditActivity
import com.example.notepad.viewModel.ActivityCalendarVM
import com.example.notepad.adapters.CalendarAdapter
import com.example.notepad.databinding.ActivityCalendarBinding
import java.util.*

class ActivityCalendar : AppCompatActivity() {

    private val activityCalendarVM: ActivityCalendarVM by lazy {
        ViewModelProvider(this)[ActivityCalendarVM::class.java]
    }

    private val convertDate = ConverterDate()

    lateinit var bindingCalendar: ActivityCalendarBinding

    private val adapterCalendar = CalendarAdapter(object : MyOnClickListener {

        override fun onClicked(position: Int) {
            activityCalendarVM.timeString =
                getString(R.string.text_selection_time_, position, position + 1)

            bindingCalendar.txSelectionTime.text = activityCalendarVM.timeString

            activityCalendarVM.timeSendInt = position
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingCalendar = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(bindingCalendar.root)
        init()

        val nowDate = Calendar.getInstance()
        activityCalendarVM.longDate =
            if (activityCalendarVM.longDate == 0L) nowDate.time.time else activityCalendarVM.longDate

        bindingCalendar.apply {
            calendarView.minDate = nowDate.time.time
            calendarView.maxDate = nowDate.time.time + 8640000000 // range to 100 days
            calendarView.date = activityCalendarVM.longDate

            txSelectionDate.text = activityCalendarVM.dateString
            txSelectionTime.text = activityCalendarVM.timeString

            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                activityCalendarVM.dateString = "Выбрана дата $dayOfMonth/${month + 1}/$year"
                activityCalendarVM.dateSendStringBd =
                    getString(R.string.date_sendEditActivity, dayOfMonth, month + 1, year)

                activityCalendarVM.longDate =
                    convertDate.convertString("$dayOfMonth/${month + 1}/$year")

                txSelectionDate.text = activityCalendarVM.dateString
            }
        }
    }

    private fun init() {
        bindingCalendar.rcView.layoutManager = LinearLayoutManager(this)
        bindingCalendar.rcView.adapter = adapterCalendar

        var counter = 0
        while (counter <= 23) {
            val selectedTime =
                SelectedTime(getString(R.string.text_selection_time_, counter, counter + 1))
            adapterCalendar.addTimeCalendar(selectedTime)
            counter++
        }
    }

    fun onClickNew(@Suppress("UNUSED_PARAMETER") view: android.view.View) {
        if(activityCalendarVM.timeSendInt == 100 && activityCalendarVM.dateSendStringBd != "")
            Toast.makeText(this,"Выберите время",Toast.LENGTH_LONG).show()
        else if (activityCalendarVM.timeSendInt != 100 && activityCalendarVM.dateSendStringBd == "")
            Toast.makeText(this,"Выберите дату",Toast.LENGTH_LONG).show()
        else if (activityCalendarVM.timeSendInt == 100 && activityCalendarVM.dateSendStringBd == "")
            Toast.makeText(this,"Выберите дату и время",Toast.LENGTH_LONG).show()
        else {
            val i = Intent(this, EditActivity::class.java)
            i.putExtra(Constants.TIME_EDIT, activityCalendarVM.timeSendInt)
            i.putExtra(Constants.DATE_STRING_CALENDAR, activityCalendarVM.dateSendStringBd)
            setResult(RESULT_OK, i)
            finish()
            startActivity(i)
        }
    }
}
