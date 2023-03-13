package com.example.dialogtest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {

    val cnt: MutableLiveData<Int> = MutableLiveData(1)
}