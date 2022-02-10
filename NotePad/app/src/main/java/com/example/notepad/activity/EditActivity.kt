package com.example.notepad.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.notepad.Constants
import com.example.notepad.dateAndTime.ConverterDate
import com.example.notepad.R
import com.example.notepad.viewModel.EditActivityVM
import com.example.notepad.databinding.EditActivityBinding
import com.example.notepad.db.MyDbManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity(){

    private lateinit var bindingEdit: EditActivityBinding

    private val converterDate =  ConverterDate()
    private val myDbManager = MyDbManager(this)

    private val editActivityVM: EditActivityVM by lazy {
        ViewModelProvider(this)[EditActivityVM::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingEdit = EditActivityBinding.inflate(layoutInflater)
        setContentView(bindingEdit.root)

        getIntents()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    fun btSendBd(@Suppress("UNUSED_PARAMETER")view: android.view.View) {
        if (bindingEdit.edTitle.text.toString() != "" && bindingEdit.edTitle.text.toString() != ""){
            CoroutineScope(Dispatchers.Main).launch {
                if (editActivityVM.isEditState)
                    myDbManager.updateItem(
                        bindingEdit.edTitle.text.toString(),
                        bindingEdit.edDesc.text.toString(),
                        editActivityVM.id
                    )
                else myDbManager.insertToDb(
                    bindingEdit.edTitle.text.toString(),
                    bindingEdit.edDesc.text.toString(),
                    editActivityVM.dateSend,
                    editActivityVM.timeSend,
                    editActivityVM.dateAndTime
                )
                finish()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun getIntents(){
        val i = intent
        bindingEdit.apply {
            if (i.getStringExtra(Constants.TITLE_KEY) == null) {

                val getDate = intent.getStringExtra(Constants.DATE_STRING_CALENDAR)
                val getTime = intent.getIntExtra(Constants.TIME_EDIT, 0)

                btAddDate.text = getString(R.string.ButtonTextEditActivity, getTime, getTime + 1) + " $getDate"
                editActivityVM.dateSend = converterDate.convertString(getDate.toString())
                editActivityVM.timeSend = converterDate.converterTimeMillis(getTime)
                editActivityVM.dateAndTime = editActivityVM.dateSend + editActivityVM.timeSend.toLong()
            } else {
                val title = intent.getStringExtra(Constants.TITLE_KEY)
                val desc = intent.getStringExtra(Constants.DESC_KEY)
                val time = intent.getIntExtra(Constants.TIME_KEY, 0)
                val date = intent.getStringExtra(Constants.DATE_KEY)
                val id = intent.getIntExtra(Constants.ID_KEY,0)

                btAddDate.text = getString(R.string.ButtonTextEditActivity, time, time + 1) + " $date"
                edTitle.setText(title)
                edDesc.setText(desc)
                editActivityVM.id = id
                editActivityVM.isEditState = true
            }
        }
    }
}
