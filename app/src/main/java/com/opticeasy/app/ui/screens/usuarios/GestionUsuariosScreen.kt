package com.opticeasy.app.ui.screens.usuarios

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.opticeasy.app.ui.components.BaseScreen
import com.opticeasy.app.ui.theme.OpticCardBg
import com.opticeasy.app.viewmodel.usuarios.UsuariosUiState
import com.opticeasy.app.viewmodel.usuarios.UsuariosViewModel

@Composable
fun GestionUsuariosScreen(
    onBack: () -> Unit,
    vm: UsuariosViewModel = viewModel()
) {
    val uiState by vm.uiState.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        vm.cargarUsuarios()
    }

    BaseScreen(contentTopPadding = 0.dp) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Gestión de usuarios",
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

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar por nick, nombre o apellidos") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            when (val state = uiState) {
                is UsuariosUiState.Idle -> {
                    Text("Preparando datos...")
                }

                is UsuariosUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UsuariosUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is UsuariosUiState.Success -> {
                    val textoBusqueda = searchText.trim().lowercase()

                    val usuariosFiltrados = state.usuarios.filter { usuario ->
                        val nombre = usuario.nombreUsuario.lowercase()
                        val apellidos = usuario.apellidosUsuario.lowercase()
                        val nick = usuario.nickUsuario.lowercase()
                        val nombreCompleto = "$nombre $apellidos"

                        textoBusqueda.isBlank() ||
                                nick.contains(textoBusqueda) ||
                                nombre.contains(textoBusqueda) ||
                                apellidos.contains(textoBusqueda) ||
                                nombreCompleto.contains(textoBusqueda)
                    }

                    if (usuariosFiltrados.isEmpty()) {
                        Text("No se encontraron usuarios.")
                    } else {
                        usuariosFiltrados.forEach { usuario ->
                            UsuarioCard(
                                idUsuario = usuario.idUsuario,
                                nombre = usuario.nombreUsuario,
                                apellidos = usuario.apellidosUsuario,
                                nick = usuario.nickUsuario,
                                rol = usuario.rol,
                                activo = usuario.activo,
                                onCambiarPassword = { nuevaPass ->
                                    vm.cambiarPassword(usuario.idUsuario, nuevaPass)
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }

            mensaje?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { vm.limpiarMensaje() }) {
                    Text("Ocultar mensaje")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Volver")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun UsuarioCard(
    idUsuario: Int,
    nombre: String,
    apellidos: String,
    nick: String,
    rol: String,
    activo: Int,
    onCambiarPassword: (String) -> Unit
) {
    var nuevaPassword by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = OpticCardBg
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "$nombre $apellidos",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "ID: $idUsuario")
            Text(text = "Nick: $nick")
            Text(text = "Rol: $rol")
            Text(
                text = if (activo == 1) "Estado: Activo" else "Estado: Inactivo"
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = nuevaPassword,
                onValueChange = { nuevaPassword = it },
                label = { Text("Nueva contraseña") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onCambiarPassword(nuevaPassword) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cambiar contraseña")
            }
        }
    }
}

