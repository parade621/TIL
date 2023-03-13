package com.example.kotlinbasic.coroutine.ch2

import kotlinx.coroutines.*

/**
 * 코루틴을 중단하는 방법에는 두가지가 있는데,
 * 1번째: 주기적으로 중단을 호출하는 것
 * 2번째: 명시적으로 중단 상태를 점검하는 것.(isActive)
 *
 * 아래 예제는 1번째 방법에 해당한다.
 */

fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default){
        var nextPrintTime = startTime
        var i =0
        while ( i< 5){
            if (System.currentTimeMillis() >= nextPrintTime){
                println("job: I'm sleeping ${i++} ... ")
                yield() // 코투틴이 취소되기 위해서는 협조적이어야 한다.
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit")
}