package com.example.app.shared_prefs_singleton.data

import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.*


/**
 * 테스트를 위한 하드 코딩
 * 추후에 Room에서 불러오는 형식으로 변경 예정
 */

object TasksRepository {

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)

    // In a real app, this would be coming from a data source like a database
    val tasks = flowOf(
        listOf(
            Task(
                name = "Go Home",
                deadline = simpleDateFormat.parse("2023-03-28")!!,
                priority = TaskPriority.LOW,
                completed = true
            ),
            Task(
                name = "Clean House",
                deadline = simpleDateFormat.parse("2023-03-28")!!,
                priority = TaskPriority.MEDIUM,
                completed = true
            ),
            Task(
                name = "Check out the code", deadline = simpleDateFormat.parse("2023-03-30")!!,
                priority = TaskPriority.LOW
            ),
            Task(
                name = "Drink some coffe", deadline = simpleDateFormat.parse("2023-03-30")!!,
                priority = TaskPriority.HIGH
            ),
            Task(
                name = "Study Two different DataStore",
                deadline = Date(),
                priority = TaskPriority.MEDIUM
            ),
            Task(
                name = "Understand how to use DataStore",
                deadline = simpleDateFormat.parse("2020-04-03")!!,
                priority = TaskPriority.HIGH
            ),
            Task(
                name = "Understand how to migrate to DataStore",
                deadline = Date(),
                priority = TaskPriority.HIGH
            )
        )
    )
}
