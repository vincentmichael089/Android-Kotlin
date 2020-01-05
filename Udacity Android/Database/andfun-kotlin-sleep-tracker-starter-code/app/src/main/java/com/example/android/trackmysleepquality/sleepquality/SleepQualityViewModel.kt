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

package com.example.android.trackmysleepquality.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import kotlinx.coroutines.*

class SleepQualityViewModel (
        val database: SleepDatabaseDao,
        private val sleepNightKey : Long = 0L
) : ViewModel(){

    private val viewModelJob = Job()// cancel all coroutines when view model is no longer in use

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    //scope determines what thread coroutines will run on
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    //navigate back to sleepTrackerFragment
    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepFragment : LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun doneNavigating(){
        _navigateToSleepTracker.value = null
    }

    // Coroutines
    fun onSetSleepQuality(quality : Int){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val tonight = database.get(sleepNightKey) ?: return@withContext
                tonight.sleepQuality = quality
                database.update(tonight)
            }
            _navigateToSleepTracker.value = true
        }
    }
}