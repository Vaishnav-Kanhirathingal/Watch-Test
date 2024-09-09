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
                else -> Log.e(TAG, "path incorrect")
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
                    .apply { this.dataMap.putString(KEY, message) }
                    .asPutDataRequest()
            )
            .addOnSuccessListener { Log.d(TAG, "addOnSuccessListener called") }
            .addOnFailureListener { Log.d(TAG, "addOnFailureListener called") }
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

//    private fun sendToSpecificNode(
//        context: Context,
//        message: String
//    ) {
//        val capabilityInfo: CapabilityInfo = Tasks.await(
//            Wearable
//                .getCapabilityClient(context)
//                .getCapability(MESSAGE_CAPABILITY_NAME, CapabilityClient.FILTER_REACHABLE)
//        )
//        val transcriptionNodeId = capabilityInfo.nodes.let { nodes ->
//            nodes.firstOrNull { it.isNearby }?.id ?: nodes.firstOrNull()?.id
//        }
//
//        if (transcriptionNodeId == null) {
//            Exception("transcription node id is null").printStackTrace()
//        } else {
//            Wearable.getMessageClient(context)
//                .sendMessage(transcriptionNodeId, TEST_PATH, "From new service func".toByteArray())
//                .addOnSuccessListener {
//                    Log.d(TAG, "addOnSuccessListener for new service function")
//                }
//                .addOnFailureListener { e ->
//                    Log.d(TAG, "addOnFailureListener for new service function")
//                    e.printStackTrace()
//                }
//        }
//    }

//    fun sendViaChannelClient(context: Context) {
//        val nodeId = "" // TODO: use correct node id
//        val wearableClient = Wearable.getChannelClient(context)
//        wearableClient.openChannel(nodeId, TEST_PATH)
//            .addOnSuccessListener { channel ->
//                Log.d(TAG, "wearableClient.openChannel(nodeId, TEST_PATH) request successful")
//                wearableClient.getOutputStream(channel)
//                    .addOnSuccessListener { outputStream ->
//                        Log.e(TAG, "wearableClient.getOutputStream(channel) successful")
//                        val data = "Hello Wearable".toByteArray()
//                        outputStream.write(data)
//                        outputStream.flush()
//                        outputStream.close()
//                    }
//                    .addOnFailureListener { e ->
//                        Log.e(TAG, "wearableClient.getOutputStream(channel) failed")
//                        e.printStackTrace()
//                    }
//            }
//            .addOnFailureListener { e ->
//                Log.e(TAG, "wearableClient.openChannel(nodeId, TEST_PATH) request failed")
//                e.printStackTrace()
//            }
//    }
//
//    fun setChannelReceiver(context: Context) {
//        Wearable.getChannelClient(context).registerChannelCallback(
//            object : ChannelClient.ChannelCallback() {
//                override fun onInputClosed(
//                    channel: ChannelClient.Channel,
//                    token: Int,
//                    reason: Int
//                ) {
//                    super.onInputClosed(channel, token, reason)
//                }
//
//                override fun onOutputClosed(
//                    channel: ChannelClient.Channel,
//                    token: Int,
//                    reason: Int
//                ) {
//                    super.onOutputClosed(channel, token, reason)
//                }
//
//                override fun onChannelOpened(channel: ChannelClient.Channel) {
//                    // Handle channel opened
//                    Wearable.getChannelClient(context)
//                        .getInputStream(channel)
//                        .addOnSuccessListener {
//                            val data = it.readBytes()
//                            Log.d(TAG, "data = ${String(data)}")
//                            it.close()
//                        }
//                        .addOnFailureListener { e ->
//                            Log.e(
//                                TAG,
//                                "Wearable.getChannelClient(context).getInputStream(channel) failed"
//                            )
//                            e.printStackTrace()
//                        }
//                }
//
//                override fun onChannelClosed(p0: ChannelClient.Channel, p1: Int, p2: Int) {
//                    super.onChannelClosed(p0, p1, p2)
//                }
//            }
//        )
//    }
}