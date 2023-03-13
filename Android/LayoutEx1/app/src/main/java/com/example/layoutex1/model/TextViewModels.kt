package com.example.layoutex1.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TextViewModels: ViewModel(){

    private val _textInput = MutableLiveData<String>()

    private val _textList = mutableListOf<String>()
    val textList:MutableList<String> get() = _textList

    fun getText(txt: String){
        _textInput.value = txt
        insertText()
    }
    private fun insertText(){
        textList.add(_textInput.value.toString())
    }
}