package com.example.kotlinbasic.coroutine.ch1

import kotlin.concurrent.thread

// ch02.kt를 coroutine이 아닌, Thread로 바꾼 것
fun main(){
        thread{
                //delay(1000L)
                Thread.sleep(1000L)
                println("World!")
        }
        println("Hello, ")
        Thread.sleep(2000L)
}