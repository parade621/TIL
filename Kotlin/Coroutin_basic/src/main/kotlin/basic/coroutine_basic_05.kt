import kotlinx.coroutines.*

/**
 * suspend
 */
fun main() = runBlocking {
    launch {
        worldPrinter()
    }
    println("Hello, ")
}

suspend fun worldPrinter(){
    delay(1000L)
    println("World!")
}