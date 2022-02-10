package com.example.notepad

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.notepad.databinding.DeleteItemBinding

object DeleteItem {
    fun showDialog(context: Context, listener:Listener){
        var dialog: AlertDialog?= null
        val builder = AlertDialog.Builder(context)
        val binding = DeleteItemBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            bDelet.setOnClickListener {
                listener.onClick(true)
                dialog?.dismiss()
            }
            bCancel.setOnClickListener {
                listener.onClick(false)
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }
    interface Listener {
    fun onClick(boolean: Boolean)
    }
}