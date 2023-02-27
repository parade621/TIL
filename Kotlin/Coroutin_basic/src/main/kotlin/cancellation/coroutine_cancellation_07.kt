package cancellation
import kotlinx.coroutines.*

/**
 * withTimeoutOrNull을 사용하면 Exception이 발생하지 않는다.
 *
 */
fun main()= runBlocking {
    val result = withTimeoutOrNull(1300L){
        repeat(1000){ i->
            println("I'm sleeping $i ... ")
            delay(500L)
        }
        "Done"
    }
    println("Result is $result")
}