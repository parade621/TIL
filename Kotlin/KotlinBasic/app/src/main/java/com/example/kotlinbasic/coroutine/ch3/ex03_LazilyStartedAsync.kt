package com.example.kotlinbasic.coroutine.ch3

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

//fun main() = runBlocking<Unit> {
//    val time = measureTimeMillis {
//        val one = async(start = CoroutineStart.LAZY){ doSomeThingUsefulOne() }
//        val two = async(start = CoroutineStart.LAZY){ doSomeThingUsefulTwo() }
//
//        one.start()
//        two.start()
//        println("The answer is ${one.await() + two.await()}")
//    }
//    println("Completed in $time ms")
//}