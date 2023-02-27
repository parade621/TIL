/**
 * coroutine이 취소되기 위해서는 조건이 필요한데, 그 조건을 알려주기 위한 예제이다.
 * 코루틴이 취소되기 위해서는 코루틴 스코프 내에 취소에 협조(Cooperative)적인 코드가 필요하다.
 * ㄴ suspend function이 있다. 이는 취소가 가능하다.
 * suspend 함수를 주기적으로 호출하는 것
 */

package cancellation
import kotlinx.coroutines.*
import java.lang.Exception

fun main() = runBlocking{
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default){
        try {
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) {
                if (System.currentTimeMillis() >= nextPrintTime) {
                    yield()
                    println("Job: I'm sleeping ${i++}...")
                    nextPrintTime += 500L
                }
            }
        }
        catch(e:Exception){
            println("Exception: [$e]")
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin() // cancel과 join을 순차적으로 불러주는 단순한 함수임.
    println("main: Now I can quit.")
}