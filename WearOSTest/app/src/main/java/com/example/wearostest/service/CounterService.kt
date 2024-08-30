package com.example.wearostest.service

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.Wearable
import kotlin.random.Random

private const val TEST_PATH = "/test/request"
private const val NODE_ID = "node_id"

class CounterService(
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

    fun startListener(context: Context) {
        Wearable.getMessageClient(context).addListener { messageEvent ->
            Log.d(TAG, "event received = ${String(messageEvent.data)}")
            when (messageEvent.path) {
                TEST_PATH -> setMessage(String(messageEvent.data))
                else -> {
                    Log.e(TAG, "path incorrect")
                }
            }
        }
        Log.d(TAG, "receiver set")
    }

//    private fun setupVoiceTranscription(context: Context) {
//        val capabilityInfo: CapabilityInfo = Tasks.await(
//            Wearable
//                .getCapabilityClient(context)
//                .getCapability(MESSAGE_CAPABILITY_NAME, CapabilityClient.FILTER_REACHABLE)
//        )
//        val transcriptionNodeId = capabilityInfo.nodes.let { nodes ->
//            nodes.firstOrNull { it.isNearby }?.id ?: nodes.firstOrNull()?.id
//        }
//
//        Wearable.getMessageClient(context)
//            .sendMessage(
//                transcriptionNodeId,
//                TEST_PATH,
//                "From new service func".toByteArray()
//            )
//            .addOnSuccessListener { Log.d(TAG, "addOnSuccessListener for new service function") }
//            .addOnFailureListener { e ->
//                Log.d(TAG, "addOnFailureListener for new service function")
//                e.printStackTrace()
//            }
//    }
}