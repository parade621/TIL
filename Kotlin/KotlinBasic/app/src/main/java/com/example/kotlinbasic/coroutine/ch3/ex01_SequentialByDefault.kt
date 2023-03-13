package com.example.kotlinbasic.coroutine.ch3

import android.os.Build.VERSION_CODES.P
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * 아래 코드처럼 작성한다면, 비동기 코드일지라도, 순차적으로 동작하게 된다.
 */

//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        val one = doSomeThingUsefulOne()
//        val two : Int = doSomeThingUsefulTwo()
//        println("The answer is ${one + two}")
//    }
//    println("Complete in $time ms")
//}
//
//suspend fun doSomeThingUsefulOne(): Int{
//    delay(1000L)
//    return 13
//}
//
//suspend fun doSomeThingUsefulTwo(): Int{
//    delay(1000L)
//    return 29
//}