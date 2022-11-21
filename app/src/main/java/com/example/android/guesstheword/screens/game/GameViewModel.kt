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
import android.util.Log


class GameViewModel : ViewModel()
{
    // The current word
    var word = MutableLiveData<String>()

    // The current score
    var score = MutableLiveData<Int>()

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init
    {
        Log.i("jj", "GameViewModel created!")
        this.resetList()
        this.nextWord()
        this.score.value = 0
    }

    override fun onCleared()
    {
        super.onCleared()
        Log.i("jj", "GameViewModel destroyed!")
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
            // gameFinished()
        } else {
            this.word.value = this.wordList.removeAt(0)
        }
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        this.score.value = this.score.value!! - 1
        this.nextWord()
    }

    fun onCorrect() {
        this.score.value = this.score.value!! + 1
        this.nextWord()
    }
}
