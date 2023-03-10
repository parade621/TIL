package com.example.kotlinbasic.coroutine.ex

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
        val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }

        // 의미있는 동작 수행...

        one.start()
        two.start()
        println("13 + 29는 ${one.await() + two.await()} 입니다.")
    }
    println("Completed in $time ms")
}

/**
 * async는 Job의 일종이기 때문에 시작 시점을 마음대로 정할 수 있다.
 * async의 생성자에다가 CoroutineStart.LAZY를 기입하면, 의미있는 다른 동작을 수행하다가 해당 코루틴의 동작이 필요한 시점에 시작할 수 있다.
 * Deffered 객체도 Job의 일종이기 때문에, start()를 통해서 코루틴 실행을 시작할 수 있고, 마찬가지로 await()를 통해 결과를 받아 이용하먄 된다.
 */

/**
 * 핵심은, LAZY로 코루틴 각각의 시작 시점을 개발자가 정할 수 있다는 것.
 */