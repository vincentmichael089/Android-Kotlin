package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Test

class StatisticsUtilsTest{

// Naming pattern for testing: subjectUnderTest_actionOrInput_resultState

    // if theres no completed task and theres 1 active task,
    // then there are 100% active task and 0% completed task
    @Test
    fun getTaskPercentage(){
        // GIVEN a list of task
        val tasks = listOf<Task>(
                Task("Title", "Description", isCompleted = false)
        )

        // WHEN you call getActiveAndCompletedStats function
        val result = getActiveAndCompletedStats(tasks)

        // THEN there are 0% of completed tasks and 100% of active tasks
        assertThat(result.completedTasksPercent, `is`(0f)) // using hamcrest so it is humanely readable
        assertEquals(100f, result.activeTasksPercent) // not using hamcrest 
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

    // App would crash if there is no task, check when there is 0 task and when there is null task
    @Test
    fun getPercentageZeroTask(){
        val tasks = listOf<Task>()

        val result = getActiveAndCompletedStats(tasks)

        assertEquals(0f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }

    @Test
    fun getPercentageNullTask(){
        val tasks = null

        val result = getActiveAndCompletedStats(tasks)

        assertEquals(0f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }
}