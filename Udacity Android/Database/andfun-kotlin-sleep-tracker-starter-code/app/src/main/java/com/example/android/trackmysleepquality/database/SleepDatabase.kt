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

package com.example.android.trackmysleepquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * entities is in list if you have more than one table supply it to entities
 * up the version number per updating schemas or app wont work
 * exportSchema true means saving version control of database to a folder. no need in this project
 */
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase(){

    abstract val sleepDatabaseDao: SleepDatabaseDao

    companion object{
        @Volatile // volatile make sure INSTANCE is always up to date
        private var INSTANCE : SleepDatabase? = null // INSTANCE make sure that we dont need to reconnect to database repeatedly

        fun getInstance(context : Context) : SleepDatabase{ //parameter context needed and return value is SleepDatabase
            //synchronized make sure that only one thread execution can enter this block one at a time
            synchronized(lock = this){
                var instance = INSTANCE

                if(instance == null){ // create database
                    instance = Room.databaseBuilder(context.applicationContext, SleepDatabase::class.java, "sleep_history_database")
                            .fallbackToDestructiveMigration() // wipe instead of migrating because it is beyond this course:(
                            .build()
                    INSTANCE =instance
                }

                return instance
            }
        }
    }
}