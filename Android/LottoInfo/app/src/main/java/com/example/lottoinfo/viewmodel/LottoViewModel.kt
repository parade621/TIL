package com.example.lottoinfo.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lottoinfo.model.LottoInfo
import com.example.lottoinfo.repository.LottoRepository
//import com.example.lottoinfo.model.Lotto
//import com.example.lottoinfo.repository.LottoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LottoViewModel(
    private val lottoRepository: LottoRepository
):ViewModel() {
    private val _getlottoResult = MutableLiveData<LottoInfo>()
    private val _setLottoResult = MutableLiveData<String>()
    val setLottoResult:LiveData<String> get() = _setLottoResult

    fun getAPIResponse(text: String) = viewModelScope.launch(Dispatchers.IO) {
        val drwNo = text.toInt()
        Log.d("viewModel",drwNo.toString())
        val response = lottoRepository.LottoInfoRequest(drwNo)
        if (response.isSuccessful){
            response.body()?.let { body->
                _getlottoResult.postValue(body)
            }
        }
        setText()
    }
    fun setText()= viewModelScope.launch(Dispatchers.Main){
        _setLottoResult.value = """
            ${_getlottoResult.value?.drwtNo1.toString()}, ${_getlottoResult.value?.drwtNo2.toString()}, ${_getlottoResult.value?.drwtNo3.toString()}, ${_getlottoResult.value?.drwtNo4.toString()}, ${_getlottoResult.value?.drwtNo5.toString()}, ${_getlottoResult.value?.drwtNo6.toString()}
        """.trimIndent()
        Log.d("ViewModel", _setLottoResult.value.toString())
    }
    }
}