package com.example.wearostest.service

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Wearable
import kotlin.random.Random

private const val MESSAGE_CAPABILITY_NAME = "message_transfer"
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
                else -> Log.e(TAG, "path incorrect")
            }
        }
        Log.d(TAG, "receiver set")
    }

    private fun sendToSpecificNode(
        context: Context,
        message: String
    ) {
        val capabilityInfo: CapabilityInfo = Tasks.await(
            Wearable
                .getCapabilityClient(context)
                .getCapability(MESSAGE_CAPABILITY_NAME, CapabilityClient.FILTER_REACHABLE)
        )
        val transcriptionNodeId = capabilityInfo.nodes.let { nodes ->
            nodes.firstOrNull { it.isNearby }?.id ?: nodes.firstOrNull()?.id
        }

        if (transcriptionNodeId == null) {
            Exception("transcription node id is null").printStackTrace()
        } else {
            Wearable.getMessageClient(context)
                .sendMessage(transcriptionNodeId, TEST_PATH, "From new service func".toByteArray())
                .addOnSuccessListener {
                    Log.d(TAG, "addOnSuccessListener for new service function")
                }
                .addOnFailureListener { e ->
                    Log.d(TAG, "addOnFailureListener for new service function")
                    e.printStackTrace()
                }
        }
    }

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