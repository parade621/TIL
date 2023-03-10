package com.example.kotlinbasic.coroutine.ex

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main()=runBlocking{// Cooutine Scope
    launch{// 새로운 코루틴 객체를 생성하는 빌더 & 해당 코루틴 동작 시작
        delay(1000L) // Non-Blocking 1초 딜레이 커맨드
        println("World!") // 1초 후에 출력된다.
    }
    println("Hello, ") // Main Coroutine은 delay 되지 않고, 이어서 수행된다.
}

/**
 * launch
 * launch는 코루틴을 만드는 빌더(Coroutine Builder)다.
 * 다른 코드들과 동시에, 독립적으로 동작하는 객체를 생성한다.
 * 즉, 해당 코루틴이 속한 쓰레드를 블로킹하지 않는다.
 * launch 스코프 내에 delay를 1초 줬기 때문에, 바깥에 있는 println("Hello!")가 먼저 동작된다.
 */

/**
 * delay
 * delay는 suspending(일시 중단) 함수이다.
 * 해당 코루틴을 특정 시간동안 일시 중단하는 기능을 수행한다.
 * 속해있는 쓰레드가 통째로 Blocking이 되는 것이 아니고, 일시 중단되는 동안 다른 코루틴이 동작하게 된다.
 */

/**
 * runBlocking
 * 이 키워드 또한 코루틴 객체를 만들게 된다.
 * 주목할 점은 코루틴 요소가 포함 되지 않는 일반적인 코드 블럭과 이 runBlocking 스코프 내의 코루틴 동작 코드 블럭들을 이어준다는 점이다.
 * 그래서 runBlocking이 중괄호로 감싸고 있는 영역 자체를 Coroutine Scope라고 한다.
 *  runBlocking을 지우게 된다면, 하위의 launch키워드가 오류를 반환한다.
 *  launch 키워드는 코루틴 객체를 생성하는 빌더이기 때문에, 반드시 runBlocking 스코프가 정의한 코루틴 스코프 내에 선언되어야 하기 때문이다.
 *  runBlocking은 속한 스레드를 해당 코루틴 스코프 내의 모든 코루틴 동작들의 실행이 완료될 때 까지 블로킹 한다는 뜻이다.
 *  실제 프로덕션 코드에서는 runBlocking을 거의 사용하지 않는다.
 *  스레드는 고비용 리소스이고, 이를 블로킹하는 것은 매우 비효율적이기 때문이다.
 */

/**
 * 코루틴은 구조화된 동시성 원칙을 따른다.
 * Constructed Concurrency란, 새로운 코루틴 객체는 반드시 코루틴의 수명을 제한하는 특정 '코루틴 스코프'내에서 실행되어야 한다는 원칙이다.
 */