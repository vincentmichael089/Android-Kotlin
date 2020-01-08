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

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()){
// code below is no need when using ListAdapter and DiffUtil Callback
//    var data = listOf<SleepNight>()
//        set(value){
//            field = value
//            notifyDataSetChanged() // this is expensive operation
//        }
//
//    override fun getItemCount(): Int {
//        return data.size
//    }

    //encapsulation of ViewHolder make sure that adapter is scalable if there is more than 1 ViewHolder to be shown by the adapter.
    override fun onBindViewHolder(holder: SleepNightAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
       // val res = holder.itemView.context.resources //moved to ViewHolder.bind function

        holder.bind(item) // right click, refactor, to function and name it bind, resource parameter then removed
    }

    // alt + enter in holder parameter, and click convert to receiver and move it to ViewHolder as a function (because ViewHolder.bind is the same as ViewHolder class having bind as an inner function)
//    private fun ViewHolder.bind(item: SleepNight, res: Resources) {
//        sleepQuality.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
//        quality.text = convertNumericQualityToString(item.sleepQuality, res)
//        qualityImage.setImageResource(when (item.sleepQuality) {
//            0 -> R.drawable.ic_sleep_0
//            1 -> R.drawable.ic_sleep_1
//            2 -> R.drawable.ic_sleep_2
//            3 -> R.drawable.ic_sleep_3
//            4 -> R.drawable.ic_sleep_4
//            5 -> R.drawable.ic_sleep_5
//            else -> R.drawable.ic_sleep_active
//        })
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepNightAdapter.ViewHolder {
        return ViewHolder.from(parent) // refactor and extract as function named from
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView){ //make the constructor private
        val sleepQuality : TextView = itemView.findViewById(R.id.sleep_length)
        val quality : TextView = itemView.findViewById(R.id.quality_string)
        val qualityImage : ImageView = itemView.findViewById(R.id.quality_image)

        fun bind(item: SleepNight) { // alt + enter in holder parameter, and click convert to receiver
            //add resource, and remove resource from function parameter
            val res = itemView.context.resources

            sleepQuality.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            quality.text = convertNumericQualityToString(item.sleepQuality, res)
            qualityImage.setImageResource(when (item.sleepQuality) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> R.drawable.ic_sleep_active
            })
        }

        companion object { //alt enter from function and set as companion object, then move to ViewHolder class
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
                return ViewHolder(view)
            }
        }
    }
}

class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>(){
    // check every item if it has the same id before and after (if 2 items have sasme id)
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

    // check if items are changed (if 2 items are equal)
    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }

}