/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

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
            context = this,
            setMessage = { message.value = it }
        )
        setContent {
            message = remember { mutableStateOf<String?>(null) }
            WearOSTestTheme {
                HomeScreen.WearApp(
                    message = message.value,
                    counterService = counterService
                )
            }
        }
        counterService.startListener()
    }
}