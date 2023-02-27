package cancellation
import kotlinx.coroutines.*

/**
 * withTimeout은 코루틴을 실행할 때 인자로 넘긴 시간이 전부 지나면 자동으로 코투틴이 종료되는 방식이다.
 * 하지만 Exception이 발생하면서 앱이 죽는다.
 */
fun main() = runBlocking {
    withTimeout(1300L){
        repeat(1000) {i->
            println("I'm sleeping $i ...")
            delay(500L)
        }
    }
}