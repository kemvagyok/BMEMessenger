package hu.bme.aut.android.bmemessenger.viewmodel

import android.content.Context
import android.widget.Toast

interface ToastMake {

    fun toastShow(text: String, context: Context)
    {
        Toast.makeText(
            context, text,
            Toast.LENGTH_SHORT).show()
    }
}