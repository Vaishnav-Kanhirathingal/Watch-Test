package com.example.wearostest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.wearostest.databinding.ActivityMainBinding
import com.example.wearostest.service.CounterService

private const val TEST_PATH = "/test/request"

class MainActivity : AppCompatActivity() {
    private var counter = 0
    private val TAG = this::class.simpleName
    private lateinit var binding: ActivityMainBinding
    private lateinit var counterService: CounterService
    private val message: MutableLiveData<String> = MutableLiveData(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        counterService = CounterService(
            context = this,
            setMessage = { message.value = it }
        )
        setContentView(binding.root)
        applyBinding()
    }

    private fun applyBinding() {
        // TODO: Not yet implemented
        try {
            counterService.startListener()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        message.observe(this) {
            binding.counterTextView.text = it?:"[MESSAGE]"
        }
        binding.testButton.setOnClickListener {
            counterService.sendTrigger(context = this)
//            counterService.sendTrigger(
//                context = this,
//                message = System.currentTimeMillis().toString()
//            )
        }
    }
}