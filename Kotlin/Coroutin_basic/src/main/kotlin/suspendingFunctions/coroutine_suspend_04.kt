//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.async
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.runBlocking
//import suspendingFunctions.doSomethingUsefulOne
//import kotlin.system.measureTimeMillis
//
//fun main(){
//    val time = measureTimeMillis {
//        val one = SomeThingUsefulOneAsync()
//        val two = SomeThingUsefulTwoAsync()
//        runBlocking {
//            println("The answer is ${one.await() + two.await()}")
//        }
//    }
//    println("Completed in $time ms")
//}
//
//fun SomeThingUsefulOneAsync() = GlobalScope.async { doSomethingUsefulOne() }
//fun SomeThingUsefulTwoAsync() = GlobalScope.async { doSomeThingUsefulTwo() }
//
///**
// * 위에 처럼 async 스타일 함수 만들지 말라는 예제.
// * 이런식으로 하면 Exception발생 시 곤란해짐.
// */
//
//suspend fun doSomeThingUsefulOne(): Int{
//    delay(1000L)
//    return 13
//}
//
//suspend fun doSomThingUsefulTwo(): Int{
//    delay(1000L)
//    return 29
//}