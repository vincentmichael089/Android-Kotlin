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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

private const val ITEM_VIEW_TYPE_HEADER = 0 // this is  used so header wont "recycled"
private const val ITEM_VIEW_TYPR_ITEM = 1

class SleepNightAdapter(val clickListener: SleepNightListener) : ListAdapter<DataItem , RecyclerView.ViewHolder>(SleepNightDiffCallback()){

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list : List<SleepNight>?){
        adapterScope.launch {
            val items = when(list){
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header)+list.map{DataItem.SleepNightItem(it)}
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }

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
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolder ->{
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(nightItem.sleepNight, clickListener)
            }
        }
   }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPR_ITEM
        }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPR_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown View Type ${viewType}")
        }
    }

    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root){ //make the constructor private //binding.root is constraint layout
        //refactor code below to inline ctrl+alt+n
        //val sleepQuality : TextView = binding.sleepLength
        //val quality : TextView = binding.qualityString
        //val qualityImage : ImageView = binding.qualityImage
        fun bind(item: SleepNight, clickListener: SleepNightListener) { // alt + enter in holder parameter, and click convert to receiver
            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object { //alt enter from function and set as companion object, then move to ViewHolder class
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding) //alt + enter di parameter binding ke view type
            }
        }
    }

    class TextViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header, parent, false)
                return TextViewHolder(view)
            }
        }
    }
}

class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>(){
    // check every item if it has the same id before and after (if 2 items have sasme id)
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    // check if items are changed (if 2 items are equal)
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class SleepNightListener(val clickListener: (sleepId : Long) -> Unit){
    fun onClick(night: SleepNight) = clickListener(night.nightId)
}

// header for recyclerview
sealed class DataItem{
    data class SleepNightItem(val sleepNight : SleepNight): DataItem(){
        override val id: Long
            get() = sleepNight.nightId
    }

    object Header : DataItem(){
        override val id: Long
            get() = Long.MIN_VALUE // so it can be passed to recyclerView with ID as minimum value of Long datatype
    }

    abstract val id : Long
}