package com.example.notepad

import com.example.notepad.dateAndTime.ConverterDate
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 * Testing class ConverterDate
 */
class ExampleUnitTest {
    private val converterDate = ConverterDate()
    @Test
    fun addition_isCorrect() {
        assertEquals(converterDate.converterTimeInt(28800000), 8)
        assertEquals(converterDate.converterTimeMillis(6), 21600000)
        assertEquals(converterDate.convertString("31/12/2021"), 1640898000000)
        assertEquals(converterDate.convertMillis(1672434000000), "31/12/2022")
    }
}