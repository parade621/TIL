package com.example.kotlinbasic.coroutine.ex

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {

    val time = measureTimeMillis {
        val one = somethingUsefulOneAsync()
        val two = somethingUsefulTwoAsync()

        runBlocking {
            println("13+29는 ${one.await() + two.await()} 입니다.")
        }
    }
    println("Completed in $time ms")
}

@OptIn(DelicateCoroutinesApi::class)
fun somethingUsefulOneAsync() = GlobalScope.async{
    doSomethingUsefulOne()
}

@OptIn(DelicateCoroutinesApi::class)
fun somethingUsefulTwoAsync() = GlobalScope.async{
    doSomethingUsefulTwo()
}

/**
 * GlobalScope의 async를 활용하여 일반 함수도 비동기적으로 동작할 수 있게 할 수 있다.
 * 비동기적으로 동작함을 명시하기 위해, 함수명 뒤에 Async를 붙인다.
 * 하지만, 위 somethingUsefulOneAsync()와 somethingUsefulTwoAsync()는 suspend function이 아니다.
 * 코투틴 스코프가 아니여도 어디서든 사용 가능하다.
 * 하지만 GlobalScope.async 키워드가 붙음으로써, 항상 비동기적으로 동시성을 띄며 동작하도록 한다.
 */

/**
 * 하지만, 위 코드는 코틀린 공식문서에서 사용하지 말 것을 당부한 스타일이다.
 * Async 스타일의 함수를 호출하는 부분과 해당 함수의 Deffered 객체의 await()를 호출하는 부분 사이에서 어떤 에러가 발생하여
 * 프로그램이 Exception을 쓰로잉하고, 프로그램이 중단되는 경우를 생각해 보겠다.
 * 일반적으로 어떤 오류 핸들러가 이 Exception을 감지해서 개발자에게 로그를 보여주는 등의 동작을 수행할 수 있고, 혹은 그냥 다른 동작을 실행하기 마련이다.
 * 하지만, Async함수는 이를 호출한 쪽은 이미 중단되었음에도 불구하고 백그라우드 상으로 계속 실행되어 있게되는 문제가 발생한다.
 * 이러한 문제는 구조적 동시성 프로그래밍(Constructed Concurrency Programming)기법에서 방지할 수 있다.
 */

