package com.example.kotlinbasic.coroutine.ch1

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main(){
    // GloablScope는 코루틴 스코프, launch는 코루틴 빌더
    // globalScope는 lifetime이 프로그램 전체인 전역 스코프이다.
    //
    GlobalScope.launch {
        delay(1000L)
        println("World!")
    }

    println("Hello,")
    Thread.sleep(2000L)
}