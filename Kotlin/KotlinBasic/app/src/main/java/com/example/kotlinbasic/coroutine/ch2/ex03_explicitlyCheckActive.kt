package com.example.kotlinbasic.coroutine.ch2

import kotlinx.coroutines.*

/**
 * 아래 예제는 앞서 작성한 코루틴을 중단하는 방법 중 두번째에 해당한다.
 */
fun main() = runBlocking{
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default){
        var nextPrintTime = startTime
        var i = 0
        while(isActive){
            if (System.currentTimeMillis() >= nextPrintTime){
                println("job: I'm sleeping ${i++}...")
                nextPrintTime += 500L
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}