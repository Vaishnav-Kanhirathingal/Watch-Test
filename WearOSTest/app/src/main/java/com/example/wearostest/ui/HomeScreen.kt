package com.example.wearostest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.example.wearostest.service.CounterService

object HomeScreen {
    @Composable
    fun WearApp(
        messageClientMessage: String?,
        dataClientMessage: String?,
        counterService: CounterService
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    space = 4.dp,
                    alignment = Alignment.CenterVertically
                ),
                content = {
                    val context = LocalContext.current
                    val textModifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                    Text(
                        modifier = textModifier,
                        textAlign = TextAlign.Center,
                        text = "message client - ${messageClientMessage ?: "[MESSAGE]"}"
                    )
                    Text(
                        modifier = textModifier,
                        textAlign = TextAlign.Center,
                        text = "data client - ${dataClientMessage ?: "[MESSAGE]"}"
                    )
                    Button(
                        onClick = {
                            counterService.sendTrigger(context = context)
                            counterService.sendViaDataClient(context = context)
                        },
                        content = {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(shape = RoundedCornerShape(corner = CornerSize(size = 4.dp)))
                                    .background(color = Color.Black)
                            )
                        }
                    )
                }
            )
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    HomeScreen.WearApp(
        messageClientMessage = null,
        dataClientMessage = null,
        counterService = CounterService(
            messageClientSetMessage = {},
            dataClientSetMessage = {}
        )
    )
}