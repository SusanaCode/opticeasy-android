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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.opticeasy.app.data.local.SessionManager
import com.opticeasy.app.data.remote.dto.clientes.ClienteUpdateRequestDto
import com.opticeasy.app.ui.components.BaseScreen
import com.opticeasy.app.viewmodel.clientes.ClienteDetalleState
import com.opticeasy.app.viewmodel.clientes.ClienteDetalleViewModel

@Composable
fun ClienteDetalleScreen(
    clienteId: Int,
    onNuevaFirmaRgpd: (Int) -> Unit,
    onNuevaRevisionGafa: (Long, String, String, String) -> Unit,
    onNuevaRevisionLc: (Long, String, String, String) -> Unit,
    onMenuPrincipal: () -> Unit,
    onListadoRevisiones: (Long, String, String) -> Unit,
    onBack: () -> Unit
) {
    val vm: ClienteDetalleViewModel = viewModel()
    val state by vm.state.collectAsState()

    LaunchedEffect(clienteId) { vm.cargar(clienteId) }

    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val rol by session.rol.collectAsState(initial = null)
    val esOptico = rol?.trim()?.equals("Optico", ignoreCase = true) == true

    val shape16 = RoundedCornerShape(16.dp)
    val blockGap = 30.dp

    // ===== Campos editables guardados al girar =====
    var nombre by rememberSaveable(clienteId) { mutableStateOf("") }
    var apellidos by rememberSaveable(clienteId) { mutableStateOf("") }
    var direccion by rememberSaveable(clienteId) { mutableStateOf("") }
    var cp by rememberSaveable(clienteId) { mutableStateOf("") }
    var poblacion by rememberSaveable(clienteId) { mutableStateOf("") }
    var provincia by rememberSaveable(clienteId) { mutableStateOf("") }
    var fechaNac by rememberSaveable(clienteId) { mutableStateOf("") }
    var dni by rememberSaveable(clienteId) { mutableStateOf("") }
    var telefono by rememberSaveable(clienteId) { mutableStateOf("") }
    var correo by rememberSaveable(clienteId) { mutableStateOf("") }
    var notas by rememberSaveable(clienteId) { mutableStateOf("") }

    // Para rellenar solo una vez por cliente
    var datosInicializados by rememberSaveable(clienteId) { mutableStateOf(false) }

    val clienteSuccess = (state as? ClienteDetalleState.Success)?.cliente

    LaunchedEffect(clienteSuccess) {
        val c = clienteSuccess ?: return@LaunchedEffect
        if (!datosInicializados) {
            nombre = c.nombre
            apellidos = c.apellidos
            direccion = c.direccion ?: ""
            cp = c.cp ?: ""
            poblacion = c.poblacion ?: ""
            provincia = c.provincia ?: ""
            fechaNac = c.fechaNacimiento ?: ""
            dni = c.dni ?: ""
            telefono = c.telefono ?: ""
            correo = c.correoElectronico ?: ""
            notas = c.notas ?: ""
            datosInicializados = true
        }
    }

    BaseScreen(contentTopPadding = 8.dp) {
        Box(Modifier.fillMaxSize()) {
            when (val s = state) {

                is ClienteDetalleState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Card(shape = shape16) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator()
                                Spacer(Modifier.width(12.dp))
                                Text("Cargando...")
                            }
                        }
                    }
                }

                is ClienteDetalleState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(blockGap))

                        Text(
                            text = "Cliente",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(blockGap))
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Spacer(Modifier.height(blockGap))

                        Text(s.message, color = MaterialTheme.colorScheme.error)

                        Spacer(Modifier.height(blockGap))

                        Button(
                            onClick = onBack,
                            shape = shape16,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text("Volver")
                        }
                    }
                }

                is ClienteDetalleState.Success -> {
                    val c = s.cliente
                    val loading = false
                    val codigo = c.idCliente.toString()
                    val rgpdFirmado = c.firmaRgpd == 1

                    val tfColors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface
                    )

                    @Composable
                    fun SectionDivider() {
                        Spacer(Modifier.height(blockGap))
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Spacer(Modifier.height(blockGap))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Ficha cliente",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outline
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = nombre,
                                onValueChange = { nombre = it },
                                label = { Text("Nombre") },
                                modifier = Modifier.weight(1f),
                                enabled = !loading,
                                colors = tfColors
                            )
                            OutlinedTextField(
                                value = apellidos,
                                onValueChange = { apellidos = it },
                                label = { Text("Apellidos") },
                                modifier = Modifier.weight(1f),
                                enabled = !loading,
                                colors = tfColors
                            )
                            OutlinedTextField(
                                value = codigo,
                                onValueChange = {},
                                enabled = false,
                                label = { Text("Código") },
                                modifier = Modifier.width(140.dp),
                                colors = tfColors
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = direccion,
                                onValueChange = { direccion = it },
                                label = { Text("Dirección") },
                                modifier = Modifier.weight(1f),
                                enabled = !loading,
                                colors = tfColors
                            )
                            OutlinedTextField(
                                value = cp,
                                onValueChange = { cp = it },
                                label = { Text("C.P.") },
                                modifier = Modifier.width(140.dp),
                                enabled = !loading,
                                colors = tfColors
                            )
                            OutlinedTextField(
                                value = poblacion,
                                onValueChange = { poblacion = it },
                                label = { Text("Población") },
                                modifier = Modifier.weight(1f),
                                enabled = !loading,
                                colors = tfColors
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedTextField(
                                value = fechaNac,
                                onValueChange = { fechaNac = it },
                                label = { Text("Fecha nacimiento") },
                                modifier = Modifier.weight(1f),
                                enabled = !loading,
                                colors = tfColors
                            )
                            OutlinedTextField(
                                value = dni,
                                onValueChange = { dni = it },
                                label = { Text("DNI") },
                                modifier = Modifier.weight(1f),
                                enabled = !loading,
                                colors = tfColors
                            )
                            OutlinedTextField(
                                value = telefono,
                                onValueChange = { telefono = it },
                                label = { Text("Teléfono") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.weight(1f),
                                enabled = !loading,
                                colors = tfColors
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = correo,
                                onValueChange = { correo = it },
                                label = { Text("Correo electrónico") },
                                modifier = Modifier.weight(1f),
                                enabled = !loading,
                                colors = tfColors
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = rgpdFirmado,
                                    onCheckedChange = null
                                )
                                Text("RGPD")
                            }

                            Button(
                                onClick = { onNuevaFirmaRgpd(clienteId) },
                                enabled = !loading,
                                shape = shape16,
                                modifier = Modifier.height(56.dp)
                            ) {
                                Text(if (rgpdFirmado) "Volver a firmar" else "Firmar RGPD")
                            }
                        }

                        SectionDivider()

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val actionBtnMod = Modifier
                                .weight(1f)
                                .height(56.dp)

                            Button(
                                onClick = {
                                    onNuevaRevisionGafa(
                                        c.idCliente.toLong(),
                                        c.nombre,
                                        c.apellidos,
                                        c.idCliente.toString()
                                    )
                                },
                                enabled = !loading && esOptico,
                                shape = shape16,
                                modifier = actionBtnMod
                            ) { Text("Nueva revisión\ngafa") }

                            Button(
                                onClick = {
                                    onNuevaRevisionLc(
                                        c.idCliente.toLong(),
                                        c.nombre,
                                        c.apellidos,
                                        c.idCliente.toString()
                                    )
                                },
                                enabled = !loading && esOptico,
                                shape = shape16,
                                modifier = actionBtnMod
                            ) { Text("Nueva revisión\nlentes de contacto") }

                            Button(
                                onClick = {
                                    onListadoRevisiones(
                                        c.idCliente.toLong(),
                                        c.nombre,
                                        c.apellidos
                                    )
                                },
                                enabled = !loading,
                                shape = shape16,
                                modifier = actionBtnMod
                            ) { Text("Listado\nrevisiones") }
                        }

                        if (!esOptico) {
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Acceso a crear revisiones solo para Óptico.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        SectionDivider()

                        OutlinedTextField(
                            value = notas,
                            onValueChange = { notas = it },
                            label = { Text("Notas del cliente") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp),
                            enabled = !loading,
                            colors = tfColors
                        )

                        Spacer(Modifier.height(blockGap))

                        Button(
                            onClick = {
                                val req = ClienteUpdateRequestDto(
                                    nombre = nombre.trim(),
                                    apellidos = apellidos.trim(),
                                    telefono = telefono.trim().ifBlank { null },
                                    direccion = direccion.trim().ifBlank { null },
                                    cp = cp.trim().ifBlank { null },
                                    poblacion = poblacion.trim().ifBlank { null },
                                    provincia = provincia.trim().ifBlank { null },
                                    fechaNacimiento = fechaNac.trim().ifBlank { null },
                                    dni = dni.trim(),
                                    correoElectronico = correo.trim().ifBlank { null },
                                    firmaRgpd = c.firmaRgpd,
                                    notas = notas.trim().ifBlank { null },
                                    activo = c.activo
                                )
                                vm.guardar(clienteId, req)
                            },
                            enabled = !loading,
                            shape = shape16,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) { Text("Guardar actualización") }

                        Spacer(Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = onMenuPrincipal,
                            enabled = !loading,
                            shape = shape16,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) { Text("Menú principal") }

                        Spacer(Modifier.height(24.dp))
                    }
                }

                else -> Unit
            }
        }
    }
}