package com.example.kotlinbasic.coroutine.ex

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    doWorld2()
}

suspend fun doWorld2() = coroutineScope{
    launch{
        delay(1000L)
        println("World")
    }
    println("Hello, ")
}

/**
 * couroutinScope라는 Coroutine Scope Builder를 활용하여, 다른 빌더들이 제공하는 코루틴 스코프 외에 따로 자신만의 코루틴 스코프를 선언할 수도 있다.
 * 이렇게 생성된 코루틴 스코프는 스코프 내의 작업들이 모두 끝날 때 까지 종료되지 않는다.
 *
 * runBlocking과 CoroutineScope는 스코프 내의 작업들이 모두 종료될 때 까지 종료되지 않는 다는 공통점이 있다.
 * 하지만, runBlocking은 해당 코루틴 객체가 속해있는 쓰레드를 블로킹하고,
 * CoroutineScope는 해당 코루틴 작업이 일시 중단되는 것 뿐, 스레드는 이외의 다른 작업을 수행가능하다는 차이점이 있다.
 * 결국 완전 다른 개념이라는 것이다.
 * 때문에 runBlocking은 Regular Function(일반적인 함수), coroutineScope는 Suspending Function(일시중단 가능한 함수)라고 부른다.
 */
