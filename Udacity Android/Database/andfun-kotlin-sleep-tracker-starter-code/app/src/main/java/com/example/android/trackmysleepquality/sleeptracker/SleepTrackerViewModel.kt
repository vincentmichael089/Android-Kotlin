/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job() // cancel all coroutines when view model is no longer in use

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    //scope determines what thread coroutines will run on
    private val uiScope = CoroutineScope(Dispatchers.Main+viewModelJob) // Main for main thread for updating UI and pass viewModelJob as job

    //data
    private val _tonight = MutableLiveData<SleepNight?>()
    private val _nights = database.getAllNights()

    // all night to string
    val stringNight = Transformations.map(_nights){
        nights -> formatNights(nights, application.resources)
    }

    // button visibilities
    val startButtonVisible = Transformations.map(_tonight){
        null == it // tonight is null at beginning, so set to visible
    }

    val stopButtonVisible = Transformations.map(_tonight){
        null != it // tonight isnt null, so set to invisible
    }

    val clearButtonVisible = Transformations.map(_nights){
        it?.isNotEmpty()
    }
    // flag navigation to sleepQuality
    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality : LiveData<SleepNight>
        get() = _navigateToSleepQuality

    fun doneNavigating(){
        _navigateToSleepQuality.value = null
    }

    //init block
    init {
        initializeTonight()
    }

    private fun initializeTonight(){
        uiScope.launch {
            _tonight.value = getTonightFromDatabase() //suspend function
        }
    }

    //launch the DAO operation in suspend function
    private suspend fun getTonightFromDatabase() : SleepNight?{
        return withContext(Dispatchers.IO){
            var night = database.getTonight()
            if(night?.endTimeMilli != night?.startTimeMilli){
                night = null
            }
            night
        }
    }

    fun onStartTracking(){
        uiScope.launch {
            val newNight = SleepNight()

            insert(newNight) //suspend function

            _tonight.value = getTonightFromDatabase()
        }
    }

    //launch the DAO operation in suspend function
    private suspend fun insert(night: SleepNight){
        withContext(Dispatchers.IO){
            database.insert(night)
        }
    }

    fun onStopTracking(){
        uiScope.launch {
            val oldNight = _tonight.value ?: return@launch // return to initialize night

            oldNight.endTimeMilli = System.currentTimeMillis() // call suspend function

            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }

    private suspend fun update(night: SleepNight){
        withContext(Dispatchers.IO){
            database.update(night)
        }
    }

    fun onClear(){
        uiScope.launch {
            clear() // call suspend function
            _tonight.value = null
        }
    }

    private suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }
}

