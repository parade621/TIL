package com.example.app.shared_prefs_singleton.data

import android.util.Log
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * 기능 테스트를 위한 하드 코딩
 * 추후에 Room에서 불러오는 형식으로 변경 예정
 */
object TasksRepository {

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    private val mutableTasksList = mutableListOf(
        Task(
            title = "Go Home",
            deadline = simpleDateFormat.parse("2023-03-28")!!.time,
            priority = TaskPriority.LOW,
            completed = true
        ),
        Task(
            title = "Clean House",
            deadline = simpleDateFormat.parse("2023-03-28")!!.time,
            priority = TaskPriority.MEDIUM,
            completed = true
        ),
        Task(
            title = "Check out the code", deadline = simpleDateFormat.parse("2023-03-30")!!.time,
            priority = TaskPriority.LOW
        ),
        Task(
            title = "Drink some coffe", deadline = simpleDateFormat.parse("2023-03-30")!!.time,
            priority = TaskPriority.HIGH
        ),
        Task(
            title = "Study Two different DataStore",
            deadline = Date().time,
            priority = TaskPriority.MEDIUM
        ),
        Task(
            title = "Understand how to use DataStore",
            deadline = simpleDateFormat.parse("2020-04-03")!!.time,
            priority = TaskPriority.HIGH
        ),
        Task(
            title = "Understand how to migrate to DataStore",
            deadline = Date().time,
            priority = TaskPriority.HIGH
        )
    )

    fun addTask(task: Task) {
        mutableTasksList.add(task)
        _tasks.value = mutableTasksList.toList() // 이거.......Flow 객체 여기서 바로 갱신해줘야함... 3시간 날림
        // 추후에 Room으로 데이터를 계속 받아오는 형식으로 작업한다면, Flow객체를 데이터 추가와 동시에 갱신해 줘야함.
        // Room에 데이터를 저장하는 것은 DataStore 구현 이후에 추가하도록할 예정이니 잘 확인할 것.
    }

    private val _tasks = MutableStateFlow(mutableTasksList.toList())

    fun getNewTask(): StateFlow<List<Task>>{
        return _tasks
    }

    fun forLog(TAG: String){
        Log.d("myTasks $TAG", "list는: $mutableTasksList \n flow는 ${_tasks.value}")
    }


}