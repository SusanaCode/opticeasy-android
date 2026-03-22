package com.opticeasy.app

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.opticeasy.app.data.local.SessionManager
import com.opticeasy.app.ui.navigation.AppNavHost
import com.opticeasy.app.ui.theme.OpticEasyTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)

        setContent {
            OpticEasyTheme {
                AppNavHost()
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        lifecycleScope.launch {
            sessionManager.updateLastActivity()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            sessionManager.updateLastActivity()
        }
    }
}