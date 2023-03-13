package com.example.kotlinbasic.coroutine.ex

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val time = measureTimeMillis {
        val one= async{ doSomethingUsefulOne() }
        val two= async{ doSomethingUsefulTwo() }
        println("13 + 29는 ${one.await() + two.await()}입니다.")
    }
    println("Completed in $time ms")
}

/**
 * launch와 형태, 용법이 비슷하다.
 * async 역시 각각 분리된 새로운 코루틴을 생성하여 다른 코루틴들과 동시에 동작 하도록 한다.
 * launch 와의 차이점이라 하면, launch는 Job 객체를 반환하는 반면, async는 Deffered 객체를 반환한다.
 * Deffered 객체는, 해당 코루틴 스코프 내에 정의된 모든 동작을 수행한 후, 어떠한 결과를 반드시 반환한다.
 * 따라서 이러한 Deffered(지연)값에 대해 await()를 사용하여 최종적인 수행결과를 얻어볼 수 있다.
 * Deffered 객체도 Job객체의 일종으로, 언제든지 작업 취소가 가능하다.
 */

/**
 * async 스코프를 통해 두 함수를 호출하고, 결과 출력에 있어 Deffered 객체 각각의 await() 메서드를 사용하게 되면
 * 명백하게 동시성이 보장되는 것을 확인할 수 있다.
 */

