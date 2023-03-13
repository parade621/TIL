package com.example.kotlinbasic.coroutine.ch1

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// runblocking은 아래와 같은 형식으로 작성하는 것이
// 가독성이 좋다.
fun main()= runBlocking {
    GlobalScope.launch{
        delay(1000L)
        println("World!")
    }
    println("Hello, ")
    delay(2000L)
}
