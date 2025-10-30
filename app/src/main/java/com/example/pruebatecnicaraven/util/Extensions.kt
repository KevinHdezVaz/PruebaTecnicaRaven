package com.example.pruebatecnicaraven.util

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}
