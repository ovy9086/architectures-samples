package org.olu.architecturesamples.rx

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import org.olu.architecturesamples.R
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.rxview_model_layout.*
import java.util.concurrent.TimeUnit

abstract class RxActivity : AppCompatActivity() {
    val subscriptions = CompositeDisposable()

    fun subscribe(disposable: Disposable): Disposable {
        subscriptions.add(disposable)
        return disposable
    }

    override fun onStop() {
        super.onStop()
        subscriptions.clear()
    }
}

class RxViewModelActivity : RxActivity() {

    private lateinit var scoreViewModelRx: ScoreViewModelRx
    private lateinit var tickViewModelRx: TickRxData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rxview_model_layout)
        scoreViewModelRx = ViewModelProviders.of(this).get(ScoreViewModelRx::class.java)
        tickViewModelRx = ViewModelProviders.of(this).get(TickRxData::class.java)
        button.setOnClickListener { scoreViewModelRx.increase() }
    }

    override fun onStart() {
        super.onStart()
        subscribeToData()
    }

    private fun subscribeToData() {
        subscribe(scoreViewModelRx.get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayScore))

        subscribe(tickViewModelRx.get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::displayTick))
    }

    private fun displayScore(it: Int) {
        text.text = it.toString()
    }

    private fun displayTick(tick: Long) {
        clockText.text = tick.toString()
    }

}

class ScoreViewModelRx(var score: Int = 0) : ViewModel() {

    private val scoreFlowable: BehaviorSubject<Int> = BehaviorSubject.createDefault(0)

    fun increase() {
        score += 1
        scoreFlowable.onNext(score)
    }

    fun get() = scoreFlowable

}

class TickRxData : ViewModel() {

    private val tickFlowable: BehaviorSubject<Long> = BehaviorSubject.createDefault(0)

    private val timer: Disposable

    init {
        Log.d("TickRxData", "created")
        timer = Flowable.interval(1000, TimeUnit.MILLISECONDS)
                .subscribe {
                    Log.d("Interval", "onNext $it")
                    tickFlowable.onNext(it)
                }
    }

    fun get() = tickFlowable

    override fun onCleared() {
        super.onCleared()
        Log.d("TickRxData", "cleared")
        timer.dispose()
    }
}