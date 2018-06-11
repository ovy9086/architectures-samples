package org.olu.architecturesamples.rx

import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.rxview_model_layout.*
import org.olu.architecturesamples.R
import java.util.concurrent.TimeUnit

//TODO: ignore for now :)

class RxMappingsViewModelActivity : RxActivity() {

    private lateinit var scoreViewModelRx: ScoreViewModelRxMappings
    private lateinit var tickViewModelRx: TickRxMappingsData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rxview_model_layout)
        scoreViewModelRx = ViewModelProviders.of(this).get(ScoreViewModelRxMappings::class.java)
        tickViewModelRx = ViewModelProviders.of(this).get(TickRxMappingsData::class.java)
        button.setOnClickListener { scoreViewModelRx.increase() }

        scoreViewModelRx.get()
                .observe(this, Observer<Int> { displayScore(it!!) })
        tickViewModelRx.get()
                .observe(this, Observer<Long> { displayTick(it!!) })
    }

    private fun displayScore(it: Int) {
        text.text = it.toString()
    }

    private fun displayTick(tick: Long) {
        clockText.text = tick.toString()
    }

}

class ScoreViewModelRxMappings(var score: Int = 0) : ViewModel() {

    private val scoreFlowable: PublishSubject<Int> = PublishSubject.create()

    init {
        scoreFlowable.onNext(0)
    }

    fun increase() {
        score += 1
        scoreFlowable.onNext(score)
    }

    fun get() = LiveDataReactiveStreams.fromPublisher<Int>(scoreFlowable.toFlowable(BackpressureStrategy.DROP))


}

class TickRxMappingsData : ViewModel() {

    private val tickFlowable = Flowable.interval(1000, TimeUnit.MILLISECONDS).doOnNext {
        Log.d("TickRxData", "Tick $it")
    }

    init {
        Log.d("TickRxData", "created")
    }

    fun get() = LiveDataReactiveStreams.fromPublisher(tickFlowable)

    override fun onCleared() {
        super.onCleared()
        Log.d("TickRxData", "cleared")
    }
}