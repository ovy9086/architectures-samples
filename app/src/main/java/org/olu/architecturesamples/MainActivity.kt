package org.olu.architecturesamples

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import org.olu.architecturesamples.rx.RxViewModelActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.olu.architecturesamples.rx.RxMappingsViewModelActivity

class MainActivity : AppCompatActivity() {

    val data = listOf(
            Data("Lifecycle", LifecycleActivity::class.java),
            Data("MediocreActivity", MediocreActivity::class.java),
            Data("ViewModel", ViewModelActivity::class.java),
            Data("ViewModelAndLiveData", ViewModelAndLiveDataActivity::class.java),
            Data("LiveDataLifeycleAwareDemo", LiveDataLifecycleAwareDemo::class.java),
            Data("RxViewModelActivity", RxViewModelActivity::class.java),
            Data("RxMappingsActivity", RxMappingsViewModelActivity::class.java)
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.adapter = ArrayAdapter<Data>(this, android.R.layout.simple_list_item_1, data)
        list.setOnItemClickListener({ _, _, position, _ ->
            startActivity(Intent(this, data[position].clazz))
        })
    }


    data class Data(val name: String, val clazz: Class<out AppCompatActivity>) {
        override fun toString(): String {
            return name
        }
    }

}
