package com.example.app.retrofit_ex.repository

import com.example.app.retrofit_ex.model.LottoInfo

import retrofit2.Response

interface LottoRepository {
    suspend fun LottoInfoRequest(
        drwNo: Int
    ):Response<LottoInfo>
}