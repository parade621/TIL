package com.example.kotlinbasic.coroutine.ex

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val time = measureTimeMillis{
        val one= async(start = CoroutineStart.LAZY){ doSomethingUsefulTwo() }
        val two= async(start = CoroutineStart.LAZY){ doSomethingUsefulTwo() }

        println("13 + 29는 ${one.await() + two.await()}입니다.")
    }
    println("Completed in $time ms")
}

/**
 * 실행해보면 2초가 걸린다. 동시성이 보장되지 않았다는 것을 알 수 있다.
 * LazyAsync1.kt와의 차이점은 start()를 호출하지 않은 것이 전부이다.
 * async를 LAZY하게 선언해 놓고 start()를 호출하지 않은 채 await()를 호출하게 된다면, 해당 코루틴의 결과가 나올 때 까지 기다리며 실행되는 특성이 있어 순차적으로 진행되어 버리기 때문이다.
 * 따라서 LAZY의 경우 상황에 맞게 적절히 사용하는 것이 중요하다.
 */