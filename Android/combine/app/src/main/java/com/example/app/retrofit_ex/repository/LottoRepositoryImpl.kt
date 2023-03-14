package com.example.app.retrofit_ex.repository

import android.util.Log
import com.example.app.retrofit_ex.model.LottoInfo
import com.example.app.retrofit_ex.net.RetrofitInstance.api
import retrofit2.Response

class LottoRepositoryImpl: LottoRepository {
    override suspend fun LottoInfoRequest(
        drwNo: Int
    ): Response<LottoInfo> {
        Log.d("repository","${drwNo} inserted")
        return api.getLottoInfo(drwNo)
    }
}