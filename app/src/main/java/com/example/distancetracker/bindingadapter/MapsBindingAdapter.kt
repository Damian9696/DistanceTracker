package com.example.distancetracker.bindingadapter

import android.view.View
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

class MapsBindingAdapter {

    companion object {

        @BindingAdapter("observeTracking")
        @JvmStatic
        fun View.observeTracking(started: Boolean?) {
            started?.let { isStarted ->
                when (this) {
                    is Button -> {
                        if (started) {
                            this.isVisible = isStarted
                        }
                    }
                    is CardView -> {
                        if (isStarted) {
                            this.isInvisible = isStarted
                        }
                    }
                }
            }

        }
    }
}