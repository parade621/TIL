package com.example.app.retrofit_ex.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app.retrofit_ex.model.LottoInfo
import com.example.app.retrofit_ex.repository.LottoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class LottoViewModel(
    private val lottoRepository: LottoRepository
) : ViewModel() {

    val latest_drwno: Int by lazy {
        calculate_latest_drwNo()
    }
    private var _inputDrwno = MutableLiveData<String>()
    val inputDrwno: LiveData<String> get() = _inputDrwno
    private val _getlottoResult = MutableLiveData<LottoInfo>()
    private val _setLottoResult = MutableLiveData<String>()
    val setLottoResult: LiveData<String> get() = _setLottoResult

    init {
        Log.d("Retrofit ViewModel", "${latest_drwno} is latest")
    }

    fun getDrwno(query: String): Boolean {
        val s = query.toInt()
        if (isPositive(s)) {
            if (s > latest_drwno) {
                Log.d("viewModel", "false!")
                return false
            } else {
                Log.d("viewModel", "true!")
                _inputDrwno.value = query
                getAPIResponse(s)
                return true
            }
        } else {
            Log.d("viewModel", "false!!")
            return false
        }
    }

    private fun isPositive(s: Int): Boolean {
        return if (s > 0) {
            true
        } else {
            false
        }
    }

    private fun getAPIResponse(drwNo: Int) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("viewModel", drwNo.toString())
        val response = lottoRepository.LottoInfoRequest(drwNo)
        if (response.isSuccessful) {
            response.body()?.let { body ->
                _getlottoResult.postValue(body)
                setText()
            }
        } else {
            _setLottoResult.value = "1 ~ ${latest_drwno} 사이의 회차를 입력해주세요."
        }
    }

    fun setText() = viewModelScope.launch(Dispatchers.Main) {
        _setLottoResult.value = """
            ${_getlottoResult.value?.drwtNo1.toString()}, ${_getlottoResult.value?.drwtNo2.toString()}, ${_getlottoResult.value?.drwtNo3.toString()}, ${_getlottoResult.value?.drwtNo4.toString()}, ${_getlottoResult.value?.drwtNo5.toString()}, ${_getlottoResult.value?.drwtNo6.toString()}
        """.trimIndent()
        Log.d("ViewModel", _setLottoResult.value.toString())
    }

    // 로또 최신회차를 계산하는 함수
    private fun calculate_latest_drwNo(): Int {
        var today = Calendar.getInstance()
        var startDate = "2002-12-07 00:00:00"
        var sf = SimpleDateFormat("yyyy-MM-dd 00:00:00")
        var date = sf.parse(startDate)

        // set date
        var calculDate = (today.time.time - date.time) / (60 * 60 * 24 * 1000)

        return ((calculDate / 7) + 1).toInt()
    }
}