package Basic

class Queue<T> {

    private val items = mutableListOf<T>()

    fun add(item: T) = items.add(item)
    fun remove() = items.removeAt(0)
    fun peek() = items[0]
    fun isEmpty() = items.isEmpty()
}

fun main(){
    val myQueue = Queue<String>()

    myQueue.add("Hello, World!")
    myQueue.add("안녕하세요!")
    println(myQueue.peek())
    myQueue.remove()
    println(myQueue.peek())
    println(myQueue.isEmpty())
}