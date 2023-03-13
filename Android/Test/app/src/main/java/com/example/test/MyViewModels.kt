package com.example.test

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModels: ViewModel() {

    private val _word : MutableLiveData<String> = MutableLiveData()
    val word:LiveData<String> get() = _word
    private val _cnt : MutableLiveData<Int> = MutableLiveData()
    val cnt:LiveData<Int> get() = _cnt
    init{
        //_word.value = "Hello"
        _cnt.value = 10
    }

    fun changeWords(){
        //_word.value="Hello, World!"
        _cnt.value = _cnt.value?.plus(1)

    }
}