package com.example.coroutine

import kotlinx.coroutines.*

fun main() = runBlocking {
    val states = arrayOf("Starting", "Doing Task 1", "Doing Task2", "Ending")
    repeat(3) {
        async{
            println("${Thread.currentThread()} has started")
            for (i in states){
                println("${Thread.currentThread()} - $i")
                delay(50)
            }
        }.await()
    }
}