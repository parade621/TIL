package com.example.kotlinbasic.coroutine.ch1

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val job = GlobalScope.launch {
        delay(3000L)
        println("World!")
    }
    println("Hello, ")
    job.join()
    /**
     * job을 생성하고, join을 하게되면, Job의 작업 수행이 모두
     * 끝날 때 까지 대기 한 후 완료된다.
     */
}