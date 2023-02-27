import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import suspendingFunctions.doSomethingUsefulOne
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) {doSomethingUsefulOne()}
        val two = async( start = CoroutineStart.LAZY) {doSomeThingUsefulTwo()}
//        one.start()
//        two.start()
        println("The answer is ${one.await() + two.await()}")
    }
    println("Completed in $time ms")
}

suspend fun doSomethingUsefultOne(): Int{
    delay(1000L)
    return 13
}

suspend fun doSomeThingUsefulTwo(): Int{
    delay(1000L)
    return 29
}

/**
 * LAZY는 start 옵션을 직접해주지 않으면, 순차적으로 실행되는군
 */