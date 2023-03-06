package com.example.layoutex1

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TextViewModels: ViewModel(){

    private val _textInput = MutableLiveData<String>()

    private val textList = mutableListOf<String>()

    fun getText(txt: String){
        _textInput.value = txt
        insertText()
    }
    private fun insertText(){
        textList.add(_textInput.value.toString())
        Log.d("wow", textList.toString())
    }
}