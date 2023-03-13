package com.example.kotlinbasic.coroutine.ex

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val time = measureTimeMillis {
        println("13 + 29 는 ${concurrentSum()}입니다.")
    }
    println("Completed in $time ms")
}

suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSomethingUsefulOne() }
    val two = async { doSomethingUsefulTwo() }
    one.await() + two.await()
}

/**
 * 위 처럼 작성하면 concurrentSum() 내부의 자식 코루틴이 스코프 둘 중 하나에게 어떠한 에러가 발생하면, 상위 코루틴 스코프 coroutineScope의 실행이 중단되어
 * 모든 자식 코루틴이 종료된다.
 * 즉, 오류가 발생하더라도 항상 상위 계층으로 전파된다.
 */