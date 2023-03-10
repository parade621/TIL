package com.example.kotlinbasic.coroutine.ex

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main()=runBlocking{
    launch{
        doWorld()
    }
    println("Hello,")
}

suspend fun doWorld(){
    delay(1000L)
    println("World!")
}

/**
 * launch 스코프 내에 정의되어 있는 코루틴 작업을 함수로 따로 빼려면 suspend 키워드를 사용하면 된다.
 * suspend 함수는 일반적인 함수들처럼 코루틴 내부에서 사용할 수 있고, 코루틴 스코프 내에서만 선언 가능한
 * suspending Function(일시중단 가능한 함수)들, 즉 delay같은 메서드들을 사용할 수 있다는 특징이 있다.
 */