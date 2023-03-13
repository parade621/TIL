package com.example.kotlinbasic.coroutine.ch1

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    launch{
        repeat(5) { i->
            println("Coroutine A, $i")
            delay(10L)
        }
    }

    launch {
        repeat(5) { i->
            println("Coroutine B, $i")
            delay(10L)
        }
    }

    println("Corouine Outer")
}