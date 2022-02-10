package com.example.notepad.dateAndTime

import java.text.SimpleDateFormat
import java.util.*

class ConverterDate{

    fun convertMillis(mil:Long):String{
        val dateLong = Calendar.getInstance()
        dateLong.timeInMillis = mil
        val days = dateLong.get(Calendar.DAY_OF_MONTH)
        val months = dateLong.get(Calendar.MONTH)
        val years = dateLong.get(Calendar.YEAR)
        return "$days/${months+1}/$years"
    }

    fun convertString(str:String):Long{
        val f = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val d = f.parse(str)
        return d!!.time
    }

    fun converterTimeMillis(int:Int):Int = if (int == 0) 0 else int*3600000

    fun converterTimeInt(int:Int):Int = if (int == 0) 0 else int/3600000
}