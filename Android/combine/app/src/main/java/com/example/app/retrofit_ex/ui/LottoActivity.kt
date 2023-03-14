package com.example.app.retrofit_ex.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.app.databinding.ActivityLottoBinding
import com.example.app.retrofit_ex.repository.LottoRepositoryImpl
import com.example.app.retrofit_ex.viewmodel.LottoViewModel
import com.example.app.retrofit_ex.viewmodel.LottoViewModelProviderFactory


class LottoActivity : AppCompatActivity() {

    private val binding : ActivityLottoBinding by lazy{
        ActivityLottoBinding.inflate(layoutInflater)
    }

    lateinit var lottoViewModel : LottoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val lottoRepository = LottoRepositoryImpl()
        val factory = LottoViewModelProviderFactory(lottoRepository)
        lottoViewModel = ViewModelProvider(this, factory)[LottoViewModel::class.java]
    }
}