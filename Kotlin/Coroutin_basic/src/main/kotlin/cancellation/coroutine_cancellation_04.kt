package cancellation
import kotlinx.coroutines.*

/**
 * 코루틴을 종료할 때 리소스를 해제하는 방법
 * 네트워크나 디비를 쓰다가 코루틴이 cancel되면 finally에서 해체해 주면 된다!
 * exception이 발생하고, 이를 이용한 방식이다.
 */

fun main()=runBlocking{
    val job = launch{
        try{
            repeat(1000){ i->
                println("job: I'm sleeping $i ...")
                delay(500L)
            }
        }finally {
            println("job: I'm running finally")
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting")
    job.cancelAndJoin()
    println("main: Now I can quit!")
}