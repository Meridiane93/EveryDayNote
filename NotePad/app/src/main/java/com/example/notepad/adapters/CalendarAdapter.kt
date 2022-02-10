package com.example.notepad.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notepad.MyOnClickListener
import com.example.notepad.R
import com.example.notepad.databinding.ModelRecyclerviewTimeBinding
import com.example.notepad.dateAndTime.SelectedTime

class CalendarAdapter(private val onClickListener: MyOnClickListener) :
    RecyclerView.Adapter<CalendarAdapter.CalendarHolder>() {

    private val listTime = ArrayList<SelectedTime>()

    inner class CalendarHolder(item: View) : RecyclerView.ViewHolder(item) {

        val bindingModelRc = ModelRecyclerviewTimeBinding.bind(item)

        fun bind(selectedTime: SelectedTime) {
            bindingModelRc.txTime.text = selectedTime.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.model_recyclerview_time, parent, false)
        return CalendarHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarHolder, position: Int) {

        holder.bind(listTime[position])
        holder.bindingModelRc.txTime.setOnClickListener {
            onClickListener.onClicked(position)
        }
    }

    override fun getItemCount(): Int = listTime.size

    fun addTimeCalendar(selectedTime: SelectedTime) =  listTime.add(selectedTime)
}