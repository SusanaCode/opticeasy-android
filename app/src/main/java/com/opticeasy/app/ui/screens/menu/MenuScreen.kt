package com.opticeasy.app.ui.screens.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opticeasy.app.ui.components.BaseScreen

@Composable
fun MenuScreen(
    onCrearCliente: () -> Unit,
    onConsultarCliente: () -> Unit,
    onCalculadoraLC: () -> Unit,
    onLogout: () -> Unit
) {
    val green = MaterialTheme.colorScheme.primary
    val shape16 = RoundedCornerShape(16.dp)

    BaseScreen(contentTopPadding = 0.dp) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Menú principal",
                    style = MaterialTheme.typography.headlineMedium,
                    color = green,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "¿Qué quieres hacer hoy?",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(32.dp))

                MenuButton(
                    text = "Crear un cliente",
                    onClick = onCrearCliente
                )

                Spacer(modifier = Modifier.height(20.dp))

                MenuButton(
                    text = "Consultar cliente",
                    onClick = onConsultarCliente
                )

                Spacer(modifier = Modifier.height(20.dp))

                MenuButton(
                    text = "Usar calculadora LC",
                    onClick = onCalculadoraLC
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedButton(
                    onClick = onLogout,
                    shape = shape16,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Cerrar sesión")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun MenuButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(text)
    }
}

