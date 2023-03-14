package com.example.app.customdialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {

    val cnt: MutableLiveData<Int> = MutableLiveData(1)
}