package cancellation
import kotlinx.coroutines.*

/**
 * 매우 특수한 케이스의 예제이다.
 * cancel 실행을 해서 코루틴이 cancel되었지만, 코루틴 내부에서 suspend 함수를 불러야하는 경우
 * finally 블록 안에서 다시 코루틴을 싨행하는 경우임.
 */
fun main()= runBlocking {
    val job = launch{
        try{
            repeat(1000) { i ->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        }finally{
            withContext(NonCancellable){ // 이런 방식으로 진행하면 됨.
                println("job: I'm running finally")
                delay(1000L)
                println("job: And I've just delayed for 1 Sec because I'm non-cancellable")
            }
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can Quit.")
}
