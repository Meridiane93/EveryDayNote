package com.example.notepad.viewModel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class ActivityMainVM:ViewModel() {

    private val cal: Calendar = Calendar.getInstance()
    @SuppressLint("SimpleDateFormat")
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    val text: String = sdf.format(cal.time)

    var dateTextInLongSendCalendarMain = text
    var dateLongSendManagerBd: Long = 0
    var btDateText =  "все заметки"
}