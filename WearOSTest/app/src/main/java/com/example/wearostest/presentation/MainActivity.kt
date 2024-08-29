package com.example.wearostest.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.wearostest.presentation.theme.WearOSTestTheme
import com.example.wearostest.service.CounterService
import com.example.wearostest.ui.HomeScreen

class MainActivity : ComponentActivity() {
    private lateinit var counterService: CounterService
    private lateinit var message: MutableState<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)
        counterService = CounterService(
            setMessage = { message.value = it }
        )
        setContent {
            message = remember { mutableStateOf(null) }
            WearOSTestTheme {
                HomeScreen.WearApp(
                    message = message.value,
                    counterService = counterService
                )
            }
        }
        counterService.startListener(context = this)
    }
}