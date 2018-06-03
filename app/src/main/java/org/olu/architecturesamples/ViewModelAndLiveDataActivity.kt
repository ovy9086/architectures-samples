package org.olu.architecturesamples

import android.arch.lifecycle.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.view_model_layout.*

class ViewModelAndLiveDataActivity : AppCompatActivity() {

    private lateinit var scoreViewModel: ScoreViewModelLiveData
    private val factory = ViewModelFactory();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_model_layout)

        scoreViewModel = ViewModelProviders.of(this, factory).get(ScoreViewModelLiveData::class.java)

        button.setOnClickListener {
            scoreViewModel.increase()
        }
        scoreViewModel.get().observe(this, Observer<Int> { t -> t?.let { displayScore(t) } })
    }

    private fun displayScore(score: Int) {
        text.text = score.toString()
    }

}


class ScoreViewModelLiveData(initialScore: Int) : ViewModel() {

    private val TAG = "ViewModel"
    private var score = MutableLiveData<Int>()

    init {
        Log.d(TAG, "ViewModel created.")
        score.value = initialScore
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel cleared.")
    }

    fun increase() {
        score.value = score.value!!.plus(1)
    }

    fun get(): LiveData<Int> {
        return score
    }
}


//This can be provided by Dagger !!! and have all the required dependencies injected
class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModelLiveData::class.java)) {
            return ScoreViewModelLiveData(0) as T
        }
        throw IllegalStateException("Unknown view model $modelClass")
    }

}