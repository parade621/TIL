/**
 * 코루틴이 중단 되는 데에 협조적인 두가지 방식 중 나머지 한가지
 * 명시적으로 상태를 체크하는 것.
 * 상태가 isActive가 아니면, 코루틴을 종료하는 방식이다.
 */

package cancellation
import kotlinx.coroutines.*

fun main()=runBlocking{
    val startTime = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var nextPrintTime = startTime
        var i = 0
        println("isActivie $isActive...")
        while (isActive){ // while이 반복하는데, isActive로 상태를 체크한다.
            if (System.currentTimeMillis() >= nextPrintTime){
                println("job: I'm sleeping ${i++}...")
                nextPrintTime += 500L
            }
        }
        println("isActivie $isActive...")
    }
    delay(1300L)
    println("main: I'm Tired of Waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit")
}
/**
 * 이 방식은 exception을 던지지 않는다.
 * 그게 앞의 방식과 차이점 중 하나임.
 */