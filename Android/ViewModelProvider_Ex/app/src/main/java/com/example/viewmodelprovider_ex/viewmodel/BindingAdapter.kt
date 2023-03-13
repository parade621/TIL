package com.example.viewmodelprovider_ex.viewmodel

import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

@BindingAdapter(value = ["progressScaled", "android:max"], requireAll = true)
fun setProgress(progressbar: ProgressBar, counter : Int, max:Int){
    progressbar.progress = (counter*2).coerceAtMost(max)
}