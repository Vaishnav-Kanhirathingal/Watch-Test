package com.example.wearostest.service

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.Wearable
import kotlin.random.Random

private const val TEST_PATH = "/test/request"
private const val NODE_ID = "node_id"

class CounterService(
    private val context: Context,
    private val setMessage: (txt: String) -> Unit
) {
    private val TAG = this::class.simpleName

    fun sendTrigger(
        context: Context,
        message: String = Random.nextInt().toString()
    ) {
        setMessage(message)
        Wearable.getMessageClient(context)
            .sendMessage(NODE_ID, TEST_PATH, message.toByteArray())
            .addOnSuccessListener { Log.d(TAG, "sendMessage success") }
            .addOnFailureListener { Log.d(TAG, "sendMessage failure");it.printStackTrace() }
    }

    fun startListener() {
        Wearable.getMessageClient(context).addListener { messageEvent ->
            when (messageEvent.path) {
                TEST_PATH -> setMessage(String(messageEvent.data))
                else -> {
                    Log.e(TAG, "path incorrect")
                }
            }
        }
        Log.d(TAG, "receiver set")
    }
}