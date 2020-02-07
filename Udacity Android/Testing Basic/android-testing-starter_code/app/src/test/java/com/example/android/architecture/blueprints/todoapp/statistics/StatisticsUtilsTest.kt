package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.junit.Assert.*
import org.junit.Test

class StatisticsUtilsTest{
    // if theres no completed task and theres 1 active task,
    // then there are 100% active task and 0% completed task
    @Test
    fun getTaskPercentage(){
        val tasks = listOf<Task>(
                Task("Title", "Description", isCompleted = false)
        )

        val result = getActiveAndCompletedStats(tasks)

        assertEquals(0f, result.completedTasksPercent)
        assertEquals(100f, result.activeTasksPercent)
    }

    // if there is 2 completed tasks and 3 active tasks
    // then there are 40% completed task and 60% active tasks
    @Test
    fun getTaskPercentage2(){
        val tasks = listOf<Task>(
                Task("Title1", "Descriptio1", isCompleted = false),
                Task("Title2", "Description2", isCompleted = false),
                Task("Title3", "Description3", isCompleted = false),
                Task("Title4", "Description4", isCompleted = true),
                Task("Title5", "Description5", isCompleted = true)
        )
        
        val result = getActiveAndCompletedStats(tasks)

        assertEquals(40f, result.completedTasksPercent)
        assertEquals(60f, result.activeTasksPercent)

    }
}