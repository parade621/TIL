package com.example.kotlinbasic.coroutine.ch1

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    repeat(100000) { i->
        launch {
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L)
}