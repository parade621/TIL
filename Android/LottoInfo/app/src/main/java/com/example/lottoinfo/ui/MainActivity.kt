package com.example.lottoinfo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.lottoinfo.R
import com.example.lottoinfo.databinding.ActivityMainBinding
import com.example.lottoinfo.repository.LottoRepositoryImpl
//import com.example.lottoinfo.repository.LottoRepositoryImpl
import com.example.lottoinfo.viewmodel.LottoViewModel
import com.example.lottoinfo.viewmodel.LottoViewModelProviderFactory

//import com.example.lottoinfo.viewmodel.LottoViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
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