package com.example.viewmodelprovider_ex.viewmodel

import android.util.Log
import androidx.lifecycle.*

class MyViewModels():ViewModel(){
    private val _counter:MutableLiveData<Int> = MutableLiveData(0)
    val cnt:LiveData<Int> get() = _counter

    init {
        _counter.value = 10
    }
    fun plusCount(){
        _counter.value = _counter.value?.plus(1)
        Log.d("wow","btn clicked!")
    }
}