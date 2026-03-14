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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
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
    var mostrarAviso by remember { mutableStateOf(false) }

    BaseScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
                texto = "No soy usuario",
                onClick = { mostrarAviso = true }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (mostrarAviso) {
        AlertDialog(
            onDismissRequest = { mostrarAviso = false },
            confirmButton = {
                TextButton(onClick = { mostrarAviso = false }) {
                    Text("Aceptar")
                }
            },
            title = {
                Text("Solicitud de acceso")
            },
            text = {
                Text("Ponte en contacto con la organización para solicitar tus credenciales de acceso.")
            }
        )
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