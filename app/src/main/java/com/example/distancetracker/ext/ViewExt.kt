package com.example.distancetracker.ext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ObjectAnimator.ofFloat
import android.animation.PropertyValuesHolder
import android.view.View
import androidx.core.view.isVisible
import kotlinx.coroutines.delay

fun View.fadeAnimation(alpha: Float, durationMilli: Long) {

    this.let { view ->
        if (alpha > 0) {
            view.alpha = 0f
            view.isVisible = true
        }

        ofFloat(this, View.ALPHA, alpha).apply {
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    if (alpha < 1) {
                        view.isVisible = false
                        view.alpha = 0f
                    }
                }
            })
            duration = durationMilli
            start()
        }
    }
}

suspend fun View.fadeAnimationWithDelay(alpha: Float, durationMilli: Long, delayMillis: Long) {
    delay(delayMillis)
    this.fadeAnimation(alpha, durationMilli)
}

fun View.scaleXYAnimation(startScale: Float, endScale: Float, durationMilli: Long) {

    this.let { view ->
        view.scaleX = startScale
        view.scaleY = startScale

        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, endScale)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, endScale)
        ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY)
            .apply {
                duration = durationMilli
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        view.scaleX = startScale
                        view.scaleY = startScale
                    }
                })
                start()
            }
    }
}
