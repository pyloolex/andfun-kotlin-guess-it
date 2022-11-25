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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import android.util.Log
import android.os.CountDownTimer


class GameViewModel : ViewModel()
{
    companion object
    {
        private const val DONE = 0L
        private const val ONE_SECOND = 1000L
        private const val COUNTDOWN_TIME = 7000L
    }

    private val timer : CountDownTimer

    // The current word
    private var _word = MutableLiveData<String>()
    val word : LiveData<String>
        get() = _word

    // The current score
    private var _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    private val _eventGameFinish = MutableLiveData<Boolean>()
    val eventGameFinish : LiveData<Boolean>
        get() = _eventGameFinish

    private val _currentTime = MutableLiveData<Long>()
    val currentTime : LiveData<Long>
        get() = _currentTime

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init
    {
        Log.i("jj", "GameViewModel created!")
        this.resetList()
        this.nextWord()
        this._score.value = 0
        this._eventGameFinish.value = false
        this._currentTime.value = GameViewModel.COUNTDOWN_TIME

        timer = object : CountDownTimer(GameViewModel.COUNTDOWN_TIME,
                                        GameViewModel.ONE_SECOND)
        {
            override fun onTick(millisUntilFinished: Long)
            {
                this@GameViewModel._currentTime.value = (
                    millisUntilFinished / GameViewModel.ONE_SECOND)
                Log.i("jj current time", millisUntilFinished.toString())
            }

            override fun onFinish()
            {
                Log.i("jj", "finished")
                this@GameViewModel._currentTime.value = GameViewModel.DONE
                this@GameViewModel._eventGameFinish.value = true
            }
        }
        timer.start()
    }

    override fun onCleared()
    {
        super.onCleared()
        Log.i("jj", "GameViewModel destroyed!")
        this.timer.cancel()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        this.wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change"
        )
        this.wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (this.wordList.isEmpty()) {
            this.resetList()
        }
        this._word.value = this.wordList.removeAt(0)
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        this._score.value = this.score.value!! - 1
        this.nextWord()
    }

    fun onCorrect() {
        this._score.value = this.score.value!! + 1
        this.nextWord()
    }

    fun onGameFinishComplete()
    {
        this._eventGameFinish.value = false
    }
}
