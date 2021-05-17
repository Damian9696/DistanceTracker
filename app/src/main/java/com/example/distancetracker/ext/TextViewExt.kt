package com.example.distancetracker.ext

import android.widget.TextView
import androidx.core.content.ContextCompat

fun TextView.changeTextColor(colorResId: Int){
    this.context?.let {
        ContextCompat.getColor(
            context,
            colorResId
        )
    }
}