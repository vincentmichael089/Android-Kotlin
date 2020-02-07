package com.example.android.architecture.blueprints.todoapp.tasks

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Config.OLDEST_SDK])
class TasksViewModelTest{

    @Test
    fun addNewTask(){
        // GIVEN a new model, add dependencies so the viewModel can get the application context
        val taskViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
        // WHEN adding a new task
        // THEN new task event is triggered
    }
}