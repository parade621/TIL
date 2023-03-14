package com.example.app.databinding

import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["app:progressScaled", "android:max"], requireAll = true)
fun setProgress(progressBar: ProgressBar, counter:Int, max:Int){
    progressBar.progress = (counter * 2).coerceAtMost(max)
}