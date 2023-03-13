package com.example.lottoinfo.model

data class LottoInfo(
    val bnusNo: Int,
    val drwNo: Int,
    val drwNoDate: String,
    val drwtNo1: Int,
    val drwtNo2: Int,
    val drwtNo3: Int,
    val drwtNo4: Int,
    val drwtNo5: Int,
    val drwtNo6: Int,
    val firstAccumamnt: Long,
    val firstPrzwnerCo: Int,
    val firstWinamnt: Long,
    val returnValue: String,
    val totSellamnt: Long
)