package com.opticeasy.app.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opticeasy.app.ui.components.BaseScreen
import com.opticeasy.app.ui.theme.OpticOnPrimary
import com.opticeasy.app.ui.theme.OpticPrimary

@Composable
fun InicioScreen(
    onLoginClick: () -> Unit,
    onCrearUsuarioClick: () -> Unit
) {
    BaseScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ===== CABECERA =====
            Text(
                text = "Bienvenido a OpticEasy",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(80.dp))

            CardOpcion(
                texto = "Ya soy usuario",
                onClick = onLoginClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            CardOpcion(
                texto = "Crear usuario",
                onClick = onCrearUsuarioClick
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CardOpcion(
    texto: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = OpticPrimary
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = texto,
                style = MaterialTheme.typography.titleLarge,
                color = OpticOnPrimary
            )
        }
    }
}