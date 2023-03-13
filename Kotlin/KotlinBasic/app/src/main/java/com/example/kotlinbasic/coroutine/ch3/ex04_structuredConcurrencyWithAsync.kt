package com.example.kotlinbasic.coroutine.ch3

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        println("The answer is ${concurrentSum()}")
//    }
//    println("Completed in $time ms")
//}
//
//suspend fun concurrentSum(): Int = coroutineScope {
//    val one = async{ doSomeThingUsefulOneDelay() }
//    val two = async{ doSomeThingUsefulTwoDelay() }
//    one.await() + two.await()
//}
//
//suspend fun doSomeThingUsefulOneDelay() : Int{
//    delay(1000L)
//    return 13
//}
//suspend fun doSomeThingUsefulTwoDelay(): Int{
//    delay(1000L)
//    return 29
//}