package com.example.kotlinbasic.coroutine.ex

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking{
    val job = launch{
        delay(1000L)
        println("World!")
    }
    println("Hello, ")
    job.join() // 스코프 내의 모든 코루틴 작업이 수행될 때 까지 대기한다.
    println("Done")
}

/**
 * launch라는 Coroutine Builder는 Job이라는 객체를 반환한다.
 * Job객체는 다양한 것을 할 수 있다.
 * 코루틴 동작을 실행
 * 현재 동작 여부 확인
 * 해당 코루틴 동작이 완료될 때 까지 끝까지 대기할 필요가 있는 경우에 사용된다.
 * 즉, Job은 코루틴을 제어할 수 있는 객체이다ㅣ
 */

/**
 * 위 코트에서 join()메서드는 코루틴을 시작하며 스코프 내의 모든 코루틴 동작이 끝날 때 까지 대기하게끔 하는 역할을 수행한다.
 * 이 외에도 Job객체는 코루틴을 제어하기 위한 다양한 기능을 제공.
 */