//package suspendingFunctions
//import kotlinx.coroutines.async
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.runBlocking
//import kotlin.system.measureTimeMillis
//
//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        val one = async{ doSomethingUsefulOne() }
//        val two = async{ doSomethingUsefulTwo() }
//        println("The answer is ${one.await() + two.await()}")
//    }
//    println("Completed in $time ms")
//}
//
//suspend fun doSomethingUsefulOne(): Int{
//    delay(1000L)
//    return 13
//}
//suspend fun doSomethingUsefulTwo(): Int{
//    delay(1000L)
//    return 29
//}
///**
// * 하지만, 더 빠른 결과를 원한다면, suspend 함수 두개를 동시에 실행시키면 되지 않을까?
// * async를 호출하게 되면, 해당 코드 블록을 실행하고, 바로 다음 라인으로 넘어가는 것이다.
// * 그리고 await를 통해 결과를 기다린다.
// */