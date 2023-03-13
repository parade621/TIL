package com.example.lottoinfo.repository

import android.util.Log
import com.example.lottoinfo.model.LottoInfo
import com.example.lottoinfo.net.RetrofitInstance.api
import retrofit2.Response

class LottoRepositoryImpl:LottoRepository {
    override suspend fun LottoInfoRequest(
        drwNo: Int
    ): Response<LottoInfo> {
        Log.d("repository","${drwNo} inserted")
        return api.getLottoInfo(drwNo)
    }
}