package com.example.wearostest.service

import android.content.Context
import android.util.Log
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import kotlin.random.Random

private const val TEST_PATH = "/test/request"
private const val NODE_ID = "node_id"

class CounterService(
    private val messageClientSetMessage: (txt: String) -> Unit,
    private val dataClientSetMessage: (txt: String) -> Unit,
) {
    private val TAG = this::class.simpleName

    fun sendTrigger(
        context: Context,
        message: String = Random.nextInt().toString()
    ) {
        messageClientSetMessage(message)
        Wearable.getMessageClient(context)
            .sendMessage(NODE_ID, TEST_PATH, message.toByteArray())
            .addOnSuccessListener { Log.d(TAG, "sendMessage success") }
            .addOnFailureListener { Log.d(TAG, "sendMessage failure");it.printStackTrace() }
    }

    fun startListener(context: Context) {
        Wearable.getMessageClient(context).addListener { messageEvent ->
            Log.d(TAG, "event received = ${String(messageEvent.data)}")
            when (messageEvent.path) {
                TEST_PATH -> messageClientSetMessage(String(messageEvent.data))
                else -> {
                    Log.e(TAG, "path incorrect")
                }
            }
        }
        Log.d(TAG, "receiver set")
    }

    private val KEY = "key"

    fun sendViaDataClient(
        context: Context,
        message: String = Random.nextInt().toString()
    ) {
        dataClientSetMessage(message)
        Wearable.getDataClient(context)
            .putDataItem(
                PutDataMapRequest
                    .create(TEST_PATH)
                    .apply { this.dataMap.putString(KEY, Random.nextInt().toString()) }
                    .asPutDataRequest()
            )
            .addOnSuccessListener { Log.d(TAG, "DataClient addOnSuccessListener called") }
            .addOnFailureListener { Log.d(TAG, "DataClient addOnFailureListener called") }
    }

    fun startDataClientListener(context: Context) {
        val client = Wearable.getDataClient(context)
        client.addListener { dataEventBuffer: DataEventBuffer ->
            for (event in dataEventBuffer) {
                when (event.type) {
                    DataEvent.TYPE_CHANGED -> {
                        val dataItem = event.dataItem
                        when (dataItem.uri.path) {
                            TEST_PATH -> DataMapItem.fromDataItem(dataItem)
                                .dataMap.getString(KEY)
                                .let { value ->
                                    Log.d(TAG, "value = $value")
                                    value?.let { dataClientSetMessage(it) }
                                }

                            else -> Log.d(TAG, "invalid path [${dataItem.uri.path}]")
                        }
                    }

                    DataEvent.TYPE_DELETED -> {
                        TODO()
                    }
                }
            }
        }
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