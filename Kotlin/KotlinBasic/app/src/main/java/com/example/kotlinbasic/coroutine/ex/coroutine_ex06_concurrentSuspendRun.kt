package com.example.kotlinbasic.coroutine.ex

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val time = measureTimeMillis {
        val result = doSomethingUsefulOne() + doSomethingUsefulTwo()
        println("13 + 29는 $result 입니다.")
    }
    println("Completed in $time ms")
}

suspend fun doSomethingUsefulOne():Int{
    delay(1000L)
    return 13
}
suspend fun doSomethingUsefulTwo(): Int{
    delay(1000L)
    return 29
}

/**
 * 위 코드에서는 두 suspend 함수가 순차적으로 진행되기 때문에, 2초가 소요된다.
 * 다음 예제에서 동일한 코드의 동시성을 보장해 보겠다.
 */
