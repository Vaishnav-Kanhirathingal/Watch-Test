package com.example.wearostest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.wearostest.databinding.ActivityMainBinding
import com.example.wearostest.service.CounterService

private const val TEST_PATH = "/test/request"

class MainActivity : AppCompatActivity() {
    private val TAG = this::class.simpleName
    private lateinit var binding: ActivityMainBinding
    private lateinit var counterService: CounterService
    private val messageMessage: MutableLiveData<String> = MutableLiveData(null)
    private val dataMessage: MutableLiveData<String> = MutableLiveData(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        counterService = CounterService(
            messageClientSetMessage = { messageMessage.value = it },
            dataClientSetMessage = { dataMessage.value = it }
        )
        setContentView(binding.root)
        applyBinding()
    }

    private fun applyBinding() {
        try {
            counterService.startListener(context = this)
            counterService.startDataClientListener(context = this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        messageMessage.observe(this) {
            binding.messageClientTextView.text = "Message client - ${it ?: "[MESSAGE]"}"
        }
        dataMessage.observe(this) {
            binding.dataClientTextView.text = "Data client - ${it ?: "[MESSAGE]"}"
        }
        binding.testButton.setOnClickListener {
            counterService.sendTrigger(context = this)
            counterService.sendViaDataClient(context = this)
        }
    }
}