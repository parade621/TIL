package com.example.lottoinfo.repository

import com.example.lottoinfo.model.LottoInfo
import retrofit2.Response

interface LottoRepository {
    suspend fun LottoInfoRequest(
        drwNo: Int
    ):Response<LottoInfo>
}