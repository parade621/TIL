package com.example.kotlinbasic.coroutine.ch3

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        val one = async { doSomeThingUsefulOne() }
//        val two = async{ doSomeThingUsefulTwo() }
//        println("The answer is ${one.await() + two.await()}")
//    }
//    println("Completed in $time ms")
//}
