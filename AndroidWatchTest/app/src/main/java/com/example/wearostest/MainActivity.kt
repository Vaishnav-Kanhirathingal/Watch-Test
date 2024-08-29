package com.example.wearostest

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.wearostest.databinding.ActivityMainBinding
import com.google.android.gms.wearable.Wearable

private const val TEST_PATH = "/test/request"

class MainActivity : AppCompatActivity() {
    private val TAG = this::class.simpleName
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyBinding()
    }

    private fun applyBinding() {
        // TODO: Not yet implemented
        try {
            setReceiver()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setReceiver() {
        Wearable.getMessageClient(this).addListener { messageEvent ->
            when (messageEvent.path) {
                TEST_PATH -> {
                    Log.d(TAG, "Path = ${messageEvent.path}, data = ${String(messageEvent.data)}")
                    binding.centerTextView.text = "message = [${String(messageEvent.data)}]"
                }

                else -> {
                    Log.d(TAG, "path incorrect")
                    binding.centerTextView.text = "path incorrect"
                }
            }
        }
        Log.d(TAG, "receiver set")
    }
}