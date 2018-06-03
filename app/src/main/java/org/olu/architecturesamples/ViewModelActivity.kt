package org.olu.architecturesamples

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.view_model_layout.*

class ViewModelActivity : AppCompatActivity() {

    private lateinit var scoreViewModel: ScoreViewModel
    //THIS CAN BE INJECTED BY DAGGER HERE
    private val factory = ViewModelsFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_model_layout)

        scoreViewModel = ViewModelProviders.of(this, factory).get(ScoreViewModel::class.java)

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

class ScoreViewModel : ViewModel() {

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

//This can be provided by Dagger !!! and have all the required dependencies injected
class ViewModelsFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            return ScoreViewModel() as T
        }
        throw IllegalStateException("Unknown view model $modelClass")
    }

}