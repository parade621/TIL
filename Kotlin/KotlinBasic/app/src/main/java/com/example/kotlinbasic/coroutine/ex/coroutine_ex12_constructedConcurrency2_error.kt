package com.example.kotlinbasic.coroutine.ex

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main():Unit = runBlocking {
    try{
        failedConucurrentSum()
    }catch(exception: ArithmeticException){
        println(exception)
        println("Computation failed with ArithmeticException")
    }
}

suspend fun failedConucurrentSum() : Int = coroutineScope {
    val one = async{
        try{
            delay(Long.MAX_VALUE) // 매우 오래걸리는 연산
            42
        }finally {
            println("First child was cancelled")
        }
    }
    val two = async<Int> {
        println("Second child throws an exception")
        throw ArithmeticException()
    }
    one.await() + two.await()
}