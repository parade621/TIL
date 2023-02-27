/**
 * 단순히 코루틴을 취소하는 예제이다.
 */
package cancellation
import kotlinx.coroutines.*

fun main() = runBlocking {
    val job = launch {
        repeat(1000) {
            println("Job: I'm sleeping $it ... ")
            delay(500L)
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancel()
    job.join()
    println("main: Now I can quit.")
}
