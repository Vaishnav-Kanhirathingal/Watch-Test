/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.wearostest.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.example.wearostest.presentation.theme.WearOSTestTheme
import com.google.android.gms.wearable.PutDataRequest
import com.google.android.gms.wearable.Wearable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearOSTestTheme {
                WearApp()
            }
        }
    }
}

@Composable
fun WearApp() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        TimeText()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                val context = LocalContext.current
                Button(
                    onClick = { test(context = context) },
                    content = {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(shape = RoundedCornerShape(corner = CornerSize(size = 4.dp)))
                                .background(color = Color.Black)
                        )
                    }
                )
                Text(text = "random text")
            }
        )
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}

//private const val VOICE_TRANSCRIPTION_CAPABILITY_NAME = "voice_transcription"
//
//private fun setupVoiceTranscription(context: Context) {
//    val capabilityInfo: CapabilityInfo = Tasks.await(
//        Wearable.getCapabilityClient(context)
//            .getCapability(
//                VOICE_TRANSCRIPTION_CAPABILITY_NAME,
//                CapabilityClient.FILTER_REACHABLE
//            )
//    )
//    updateTranscriptionCapability(capabilityInfo).also { capabilityListener ->
//        Wearable.getCapabilityClient(context).addListener(
//            capabilityListener,
//            VOICE_TRANSCRIPTION_CAPABILITY_NAME
//        )
//    }
//}
//private var transcriptionNodeId: String? = null
//
//private fun updateTranscriptionCapability(capabilityInfo: CapabilityInfo) {
//    transcriptionNodeId = pickBestNodeId(capabilityInfo.nodes)
//}
//
//private fun pickBestNodeId(nodes: Set<Node>): String? {
//    // Find a nearby node or pick one arbitrarily.
//    return nodes.firstOrNull { it.isNearby }?.id ?: nodes.firstOrNull()?.id
//}

private const val TEST_PATH = "/test/request"
private const val TAG = "MainActivity"

fun test(context: Context) {
//    Wearable.getMessageClient(context)
//        .sendMessage(TEST_PATH, TAG, "some message".toByteArray())

    Wearable.getDataClient(context)
        .putDataItem(
            PutDataRequest.create(TEST_PATH)
                .setData(
                    "\"~Some jumbled words~\" - Wear OS".toByteArray()
                )
        )
        .addOnSuccessListener { Log.d(TAG, "success") }
        .addOnFailureListener { Log.d(TAG, "failure");it.printStackTrace() }
}