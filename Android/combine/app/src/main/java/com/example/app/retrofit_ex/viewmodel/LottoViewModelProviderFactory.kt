package com.example.app.retrofit_ex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.app.retrofit_ex.repository.LottoRepository


@Suppress("UNCHECKED_CAST")
class LottoViewModelProviderFactory(
    private val lottoRepository: LottoRepository
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LottoViewModel::class.java)){
            return LottoViewModel(lottoRepository) as T
        }
        throw IllegalArgumentException("ViewModel class not found")
    }
}