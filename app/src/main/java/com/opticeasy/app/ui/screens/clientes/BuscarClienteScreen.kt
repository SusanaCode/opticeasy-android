package com.opticeasy.app.ui.screens.clientes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.opticeasy.app.ui.components.BaseScreen
import com.opticeasy.app.ui.theme.OpticSecondary
import com.opticeasy.app.viewmodel.clientes.ClientesBuscarState
import com.opticeasy.app.viewmodel.clientes.ClientesBuscarViewModel

@Composable
fun BuscarClienteScreen(
    vm: ClientesBuscarViewModel,
    onShowListado: () -> Unit,
    onMenuPrincipal: () -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var dni by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var codigoCliente by rememberSaveable { mutableStateOf("") }
    val state by vm.state.collectAsState()
    val loading = state is ClientesBuscarState.Loading
    val shape16 = RoundedCornerShape(16.dp)

    LaunchedEffect(state) {
        if (state is ClientesBuscarState.Success) {
            onShowListado()
        }
    }

    BaseScreen(contentTopPadding = 8.dp) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Búsqueda Cliente",
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

                val tfColors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading,
                    colors = tfColors
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    label = { Text("Apellidos") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading,
                    colors = tfColors
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = dni,
                    onValueChange = { dni = it },
                    label = { Text("DNI") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading,
                    colors = tfColors
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading,
                    colors = tfColors
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = codigoCliente,
                    onValueChange = { codigoCliente = it },
                    label = { Text("Código cliente") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading,
                    colors = tfColors
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        vm.buscar(
                            nombre = nombre,
                            apellidos = apellidos,
                            dni = dni,
                            telefono = telefono,
                            codigoCliente = codigoCliente
                        )
                    },
                    enabled = !loading,
                    shape = shape16,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Buscar")
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (state is ClientesBuscarState.Error) {
                    Text(
                        text = (state as ClientesBuscarState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onMenuPrincipal,
                    enabled = !loading,
                    shape = shape16,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Menú principal")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = OpticSecondary)
                }
            }
        }
    }
}