package com.example.kotlinbasic.coroutine.ex

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    doWorld3()
    println("Done")
}

// launch를 통해 선언한 두 코루틴 작업이 동시에 진행되는 함수
suspend fun doWorld3() = coroutineScope {
    launch{
        delay(2000L)
        println("World 3") // 2초 딜레이 후 실행된다 - 3번째로 수행
    }
    launch{
        delay(1000L)
        println("World 4") // 1초 딜레이 후 실행된다 - 2번째로 수행
    }
    println("Hello")// 앞 작업들이 모두 delay 상태이기 때문에, 메인 코루틴 작업으로서 첫번째로 수행된다.
}

/**
 * runBlocking 스코프 내에서는 별다른 동시정 처리가 없기 때문에 doWorld3()가 실행된 ㅎ, "Done"을 출력하도록 한다.(순차적)
 * doWorld3() 스코프 내부에는, 두 가지 코루틴 작업들이 있으며, 이들은 완전히 동시성이 보장되기 때문에, suspending Function의 제어에 따라 알맞는 출력 결과를 표시한다.
 */