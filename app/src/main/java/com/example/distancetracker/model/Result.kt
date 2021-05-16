package com.example.distancetracker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Result(
    val distance: String,
    val time: String
) : Parcelable
