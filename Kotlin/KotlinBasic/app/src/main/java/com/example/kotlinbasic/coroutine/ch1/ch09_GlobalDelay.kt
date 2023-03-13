package com.example.kotlinbasic.coroutine.ch1

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    GlobalScope.launch{
        repeat(1000){ i->
            println("I'm sleeping &i ... ")
            delay(500L)
        }
    }
    delay(1300L)
}
// GlobalScope가 끝나는 순간, 내부에 남은 코루틴 작업의 수와는
// 상관 없이 종료