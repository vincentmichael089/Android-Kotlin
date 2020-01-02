/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    //ViewModel
    private lateinit var viewModel : GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        //The ViewModel is a stable place to store the data to display in the associated UI controller.
        //The Fragment draws the data on screen and captures input events. It should not decide what to display on screen or process what happens during an input event.
        //The ViewModel never contains references to activities, fragments, or views.
        Log.i("GameFragment", "Called ViewModelProviders.of!")
        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        binding.gameViewModel = viewModel //let xml access kotlin code (check score_fragment.xml onclick), let view communicate directly with viewmodel
        binding.lifecycleOwner = this //databinding from livedata needs to know lifecycle

        //LiveData Observer, commented because now we are using livedata binding
//        viewModel.score.observe(this, Observer {
//            newScore ->  binding.scoreText.text = newScore.toString()
//        })
//
//        viewModel.word.observe(this, Observer {
//            newWord -> binding.wordText.text = newWord.toString()
//        })

//        viewModel.eventGameFinish.observe(this, Observer {
//            hasFinished -> if(hasFinished){
//            gameFinished()
//            viewModel.onGameFinishComplete()
//        }
//        })

        viewModel.currentTime.observe(this, Observer {
            newTime -> binding.timerText.text = DateUtils.formatElapsedTime(newTime)
        })

        return binding.root

    }


    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore(viewModel.score.value ?: 0) //if null then set score to 0
        findNavController(this).navigate(action)
        //Toast.makeText(this.activity, "Game has finished", Toast.LENGTH_LONG).show()
    }



}