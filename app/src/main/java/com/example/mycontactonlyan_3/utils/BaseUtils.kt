package com.example.mycontactonlyan_3.utils

import android.os.Message
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText

fun Fragment.showToast(message: String){
    Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
}
fun TextInputEditText.text() = this.text.toString()