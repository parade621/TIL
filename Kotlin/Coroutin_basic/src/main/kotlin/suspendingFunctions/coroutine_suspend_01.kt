//import kotlinx.coroutines.delay
//import kotlinx.coroutines.runBlocking
//import kotlin.system.measureTimeMillis
//
//fun main() = runBlocking{
//    val time = measureTimeMillis {
//        val one = doSomethingUsefulOne()
//        val two = doSomethingUsefulTwo()
//        println("The answer is ${one + two}")
//    }
//    println("Completed in $time ms")
//}
//
//suspend fun doSomethingUsefulOne(): Int{
//    delay(1000L) // 서버를 호출하던지, 해비한 연산을 진행하는 코드가 들어오는 자리(공식문서 피셜)
//    // 예를 들면, 레트로핏을 호출한다던지 그런 위치임.
//    return 13
//}
//suspend fun doSomethingUsefulTwo(): Int{
//    delay(1000L)
//    return 29
//}
//
///**
// * 이 예제는 코루틴의 실행 순서를 보여주는 예제임.
// * 코루틴에서는 위와 같이 코드를 작성하면, 순차적으로 실행되는 것이 기본임.
// * 드림코드에 가까움.
// *
// */