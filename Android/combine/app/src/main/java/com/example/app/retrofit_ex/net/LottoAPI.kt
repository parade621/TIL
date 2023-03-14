package com.example.lottoinfo.net

import com.example.lottoinfo.model.LottoInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=1010
interface LottoAPI {
    @GET("/common.do?method=getLottoNumber")
    suspend fun getLottoInfo(
        @Query("drwNo") drwNo: Int,
    ): Response<LottoInfo>
}