package org.olu.architecturesamples

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.view_model_layout.*
import java.util.Timer
import java.util.TimerTask

class LiveDataLifecycleAwareDemo : AppCompatActivity() {

    private lateinit var tickLiveData: TickLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_model_layout)

        tickLiveData = ViewModelProviders.of(this).get(TickLiveData::class.java)


        tickLiveData.get().observe(this, Observer<Int> { t -> t?.let { displayData(t) } })
    }

    private fun displayData(score: Int) {
        Log.d("Activity", "updatingData")
        text.text = score.toString()
    }
}

class TickLiveData : ViewModel() {

    private val TAG = "ViewModel"
    private var timer = Timer()
    private val data = MutableLiveData<Int>()


    init {
        Log.d(TAG, "ViewModel created.")
        data.value = 0
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val nextData = data.value!! + 1
                data.postValue(nextData)
            }

        }, 1000, 1000)
    }

    override fun onCleared() {
        super.onCleared()
        timer.purge()
        timer.cancel()
        Log.d(TAG, "ViewModel cleared.")
    }


    fun get(): LiveData<Int> {
        return data
    }
}
