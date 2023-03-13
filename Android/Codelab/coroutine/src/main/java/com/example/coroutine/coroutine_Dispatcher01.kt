package com.example.coroutine

import kotlinx.coroutines.*

fun main()= runBlocking {
    repeat(3) {
        GlobalScope.launch {
            println("Hi from ${Thread.currentThread()}")
        }
    }
}