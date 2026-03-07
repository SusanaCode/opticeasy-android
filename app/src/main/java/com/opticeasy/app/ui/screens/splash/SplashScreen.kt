package com.opticeasy.app.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.opticeasy.app.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1000)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7F6)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_opticeasy_pdf),
            contentDescription = "OpticEasy",
            modifier = Modifier
                .width(320.dp)
        )
    }
}

