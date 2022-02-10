package com.example.notepad.viewModel

import androidx.lifecycle.ViewModel

class ActivityCalendarVM:ViewModel() {

    //sendForEdit
    var timeSendInt = 100
    var dateSendStringBd = ""


    var dateString = "Выберите дату"
    var timeString = "Выберите время"

    var longDate: Long = 0
}