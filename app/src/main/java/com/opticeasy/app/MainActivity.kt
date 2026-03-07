package com.opticeasy.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.opticeasy.app.ui.navigation.AppNavHost
import com.opticeasy.app.ui.theme.OpticEasyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            OpticEasyTheme {
                AppNavHost()
            }
        }
    }
}
