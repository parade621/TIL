package Basic

class Queue<T> {
    private val items = mutableListOf<T>()

    fun add(item: T) = items.add(item)
    fun remove() = items.removeAt(0)
    fun peek() = items[0]
    fun isEmpty() = items.isEmpty()
}