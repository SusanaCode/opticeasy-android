package com.opticeasy.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.opticeasy.app.R

@Composable
fun BaseScreen(
    contentTopPadding: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 0.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(id = R.drawable.logo_opticeasy),
                contentDescription = "Logo OpticEasy",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(contentTopPadding))

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top, // 👈 CAMBIO CLAVE
                content = content
            )
        }
    }
}

