package com.example.navnsharedviewmodel.viewmodel

import androidx.lifecycle.MutableLiveData

data class MenuItem(
    val name: String,
    val price: Int,
    val type : Int,
    val cnt: MutableLiveData<Int>
){
    fun reset(){
        cnt.value = 1
    }
}
