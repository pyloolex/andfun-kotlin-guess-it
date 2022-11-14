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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding
import timber.log.Timber

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    // The current word
    private var word = ""

    // The current score
    private var score = 0

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

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

        if (savedInstanceState != null)
        {
            this.word = savedInstanceState.getString("word")!!
            this.score = savedInstanceState.getInt("score")
            this.wordList =
                savedInstanceState.getStringArrayList("list")!!.toMutableList()
        }
        else
        {
            resetList()
            nextWord()
        }

        binding.correctButton.setOnClickListener { onCorrect() }
        binding.skipButton.setOnClickListener { onSkip() }
        updateScoreText()
        updateWordText()
        return binding.root

    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change"
        )
        wordList.shuffle()
    }

    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        Timber.i("jj here 1")
        val action = GameFragmentDirections.actionGameToScore(score)
        Timber.i("jj here 2")
        findNavController(this).navigate(action)
        Timber.i("jj here 3")
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        Timber.i("jj next Word")
        if (wordList.isEmpty()) {
            gameFinished()
            Timber.i("jj finished")
        } else {
            Timber.i("jj one more")
            word = wordList.removeAt(0)
        }
        updateWordText()
        updateScoreText()
    }

    /** Methods for buttons presses **/

    private fun onSkip() {
        score--
        nextWord()
    }

    private fun onCorrect() {
        score++
        nextWord()
    }

    /** Methods for updating the UI **/

    private fun updateWordText() {
        binding.wordText.text = word

    }

    private fun updateScoreText() {
        binding.scoreText.text = score.toString()
    }

    override fun onSaveInstanceState(outBundle: Bundle)
    {
        outBundle.putString("word", this.word)
        outBundle.putInt("score", this.score)
        outBundle.putStringArrayList("list", ArrayList<String>(wordList))
        Timber.i("jj saved")
    }
}
