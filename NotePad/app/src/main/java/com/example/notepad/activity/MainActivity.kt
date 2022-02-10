package com.example.notepad.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.AlarmReceiver
import com.example.notepad.Constants
import com.example.notepad.DeleteItem
import com.example.notepad.adapters.MainAdapter
import com.example.notepad.databinding.ActivityMainBinding
import com.example.notepad.dateAndTime.ActivityCalendar
import com.example.notepad.dateAndTime.CalendarMainActivity
import com.example.notepad.db.MyDbManager
import com.example.notepad.viewModel.ActivityMainVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var launcher: ActivityResultLauncher<Intent>? = null
    private lateinit var bindingMain: ActivityMainBinding
    private var job: Job ?= null

    val myDbManager = MyDbManager(this)
    val mainAdapter = MainAdapter(ArrayList(),this)

    private var listDateTime = mutableListOf<Long>()

    private val activityMainVM: ActivityMainVM by lazy {
        ViewModelProvider(this)[ActivityMainVM::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        bindingMain.addDateMain.text = activityMainVM.btDateText

        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    activityMainVM.dateLongSendManagerBd = result.data?.getLongExtra(Constants.DATE_LONG, 0)!!

                    activityMainVM.btDateText = result.data?.getStringExtra(Constants.DATE_STRING_CALENDAR)!!
                    bindingMain.addDateMain.text = activityMainVM.btDateText
                    activityMainVM.dateTextInLongSendCalendarMain =  if(bindingMain.addDateMain.text != "все заметки") activityMainVM.btDateText else activityMainVM.text
                    initSearchView()
                }
            }
        init()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter("${activityMainVM.dateLongSendManagerBd}")
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    fun calendar(@Suppress("UNUSED_PARAMETER")view: View) {
        val intent = Intent(this, CalendarMainActivity::class.java)
        intent.putExtra(Constants.DATE_STRING,activityMainVM.dateTextInLongSendCalendarMain)
        launcher?.launch(intent)
    }

    fun onClickCalendar(@Suppress("UNUSED_PARAMETER")view2: View) {
        val i = Intent(this, ActivityCalendar::class.java)
        startActivity(i)
    }

    private fun init(){
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(bindingMain.rcView)

        bindingMain.rcView.layoutManager = LinearLayoutManager(this)
        bindingMain.rcView.adapter = mainAdapter
    }

    private fun fillAdapter(text:String){
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            val list = myDbManager.readDbData(text)
            mainAdapter.updateAdapter(list)

            bindingMain.tvNoElements.visibility = if (list.size > 0) View.GONE
            else View.VISIBLE
        }
        listDateTime = myDbManager.readDbDataTime(text)

        arrayListDate()
    }

    private fun getSwapMg(): ItemTouchHelper{
        return ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { // Dialog show
                DeleteItem.showDialog(this@MainActivity, object : DeleteItem.Listener {
                    override fun onClick(boolean: Boolean) {
                        if (boolean) mainAdapter.removeItem(viewHolder.adapterPosition, myDbManager)
                        else mainAdapter.removeItemNoDelete(viewHolder.adapterPosition)
                    }
                })
            }
        })
    }
    private fun initSearchView(){
        fillAdapter("${activityMainVM.dateLongSendManagerBd}")
    }

    private fun arrayListDate(){ // check time for notification
        val calendar: Calendar = Calendar.getInstance()
        val list = listDateTime.sorted()
        val listNewDate = mutableListOf<Long>()
        if(list.isNotEmpty()) {
            list.forEach {
                if (it >= calendar.time.time) listNewDate.add(it)
            }
           if (listNewDate.isNotEmpty()) initOnAlarm(listNewDate[0])
        }
    }

    companion object {
        const val ALARM_REQUEST_CODE = 1000
    }

    @SuppressLint("InlinedApi")
    private fun initOnAlarm(long: Long) { // show the notification at the specified time

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE,
                    intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setExact(AlarmManager.RTC_WAKEUP,long,pendingIntent)
    }
}