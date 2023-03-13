package com.example.kotlinbasic.coroutine.ch1

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking{
    launch{
        myWorld()
    }
    println("Hello, ")
}

suspend fun myWorld(){
    delay(1000L)
    println("world!")
}