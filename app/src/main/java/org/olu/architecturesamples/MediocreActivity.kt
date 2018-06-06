package org.olu.architecturesamples

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.view_model_layout.*

class MediocreActivity : AppCompatActivity() {

    private lateinit var scoreViewModel: MediocreScoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_model_layout)

        scoreViewModel = MediocreScoreViewModel() // OR MAYBE INJECTED BY DAGGER

        button.setOnClickListener {
            scoreViewModel.increase()
            displayScore()
        }

        displayScore()

    }

    private fun displayScore() {
        text.text = this.scoreViewModel.get().toString()
    }

}

class MediocreScoreViewModel : ViewModel() {

    private val TAG = "ViewModel"
    private var score = 0

    init {
        Log.d(TAG, "ViewModel created.")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel cleared.")
    }

    fun increase() {
        score += 1
    }

    fun get(): Int {
        return score

    }
}