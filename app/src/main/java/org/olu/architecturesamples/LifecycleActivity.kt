package org.olu.architecturesamples

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class LifecycleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(LocationListener(
                lifecycle,
                this.getSystemService(Context.LOCATION_SERVICE) as LocationManager,
                { handleLocation(it) }))
    }

    fun handleLocation(location: Location) {
    }
}


//A better example would probably here a Video Player
class LocationListener(val lifecycle: Lifecycle,
                       val locationManager: LocationManager,
                       val callback: (location: Location) -> Unit) : LifecycleObserver {


    val TAG = "LocationListener"

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.d(TAG, "onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        Log.d(TAG, "onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        Log.d(TAG, "onStop")
    }

    fun requestUpdates() {

        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            //Request updates
        }
    }
}