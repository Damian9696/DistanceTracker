package com.example.distancetracker.util

import android.animation.ObjectAnimator.ofFloat
import android.view.View

fun View.fadeAnimation(alpha: Float, durationMilli: Long) {
    ofFloat(this, View.ALPHA, alpha).apply {
        duration = durationMilli
        start()
    }
}
