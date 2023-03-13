package com.example.kotlinbasic.coroutine.ch1

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(){
    GlobalScope.launch {
        delay(1000L)
        println("World!")
    }
    println("Hello, ")

    // runBLocking은 자신을 호출한 스레드를 Blocking한다.
    runBlocking{
        delay(2000L)
    }
}