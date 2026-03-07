package com.opticeasy.app.ui.screens.clientes

import android.app.DatePickerDialog
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
import com.opticeasy.app.data.remote.dto.clientes.ClienteCreateRequestDto
import com.opticeasy.app.ui.components.BaseScreen
import com.opticeasy.app.viewmodel.clientes.ClientesUiState
import com.opticeasy.app.viewmodel.clientes.ClientesViewModel
import java.util.Calendar
import java.util.Locale

@Composable
fun ClientesScreen(
    onMenuPrincipal: () -> Unit,
    onFirmaRgpd: (Int) -> Unit,
    vm: ClientesViewModel = viewModel()
) {
    val state by vm.state.collectAsState()


    // ====== Campos formulario ======
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var cp by rememberSaveable { mutableStateOf("") }
    var poblacion by rememberSaveable { mutableStateOf("") }
    var provincia by rememberSaveable { mutableStateOf("") }

    // Fecha: ISO para backend + UI en español para mostrar
    var fechaNacimientoIso by rememberSaveable { mutableStateOf("") } // YYYY-MM-DD
    var fechaNacimientoUi by rememberSaveable { mutableStateOf("") }  // dd/MM/yyyy

    var dni by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }

    // Validación local
    var errorLocal by rememberSaveable { mutableStateOf<String?>(null) }

    // Guardamos el ID del cliente creado para habilitar RGPD
    var clienteCreadoId by rememberSaveable { mutableStateOf<Int?>(null) }
    var mensajeOk by rememberSaveable { mutableStateOf<String?>(null) }

    // Control de botones: Guardar deshabilitado tras pulsar / tras éxito
    var guardarBloqueado by rememberSaveable { mutableStateOf(false) }

    // ====== Estado VM ======
    val loading = state is ClientesUiState.Loading
    val errorApi = (state as? ClientesUiState.Error)?.message

    // Cuando llega Success, guardamos el id y mostramos botón RGPD (sin navegar)
    LaunchedEffect(state) {
        val s = state
        when (s) {
            is ClientesUiState.Success -> {
                clienteCreadoId = s.idCliente
                mensajeOk = "Cliente creado (ID: ${s.idCliente}). Ya puedes firmar RGPD."
                guardarBloqueado = true // queda deshabilitado definitivamente tras crear
                vm.resetState()
            }
            is ClientesUiState.Error -> {
                // Si falla, volvemos a habilitar Guardar para reintentar
                guardarBloqueado = false
            }
            else -> Unit
        }
    }

    // ====== DatePicker ======
    val context = LocalContext.current
    val cal = remember { Calendar.getInstance() }

    fun abrirDatePicker() {
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, y, m, d ->
                // ISO para backend
                fechaNacimientoIso = String.format(Locale.US, "%04d-%02d-%02d", y, m + 1, d)
                // UI España
                fechaNacimientoUi = String.format(Locale("es", "ES"), "%02d/%02d/%04d", d, m + 1, y)
            },
            year, month, day
        ).show()
    }

    fun validarObligatorios(): String? {
        if (nombre.trim().isEmpty()) return "Nombre es obligatorio"
        if (apellidos.trim().isEmpty()) return "Apellidos es obligatorio"
        if (dni.trim().isEmpty()) return "DNI es obligatorio"
        return null
    }

    // Scroll
    val scrollState = rememberScrollState()

    BaseScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ===== Cabecera =====
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Creación Cliente",
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

            // ===== Formulario =====
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading && !guardarBloqueado,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = apellidos,
                onValueChange = { apellidos = it },
                label = { Text("Apellidos *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading && !guardarBloqueado,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading && !guardarBloqueado,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading && !guardarBloqueado,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = cp,
                    onValueChange = { cp = it },
                    label = { Text("Código postal") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    enabled = !loading && !guardarBloqueado,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )

                OutlinedTextField(
                    value = poblacion,
                    onValueChange = { poblacion = it },
                    label = { Text("Población") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    enabled = !loading && !guardarBloqueado,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = provincia,
                onValueChange = { provincia = it },
                label = { Text("Provincia") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading && !guardarBloqueado,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = fechaNacimientoUi,
                onValueChange = { /* readOnly */ },
                label = { Text("Fecha nacimiento") },
                singleLine = true,
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading && !guardarBloqueado,
                trailingIcon = {
                    TextButton(
                        onClick = { abrirDatePicker() },
                        enabled = !loading && !guardarBloqueado
                    ) { Text("Elegir") }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = dni,
                onValueChange = { dni = it },
                label = { Text("DNI *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading && !guardarBloqueado,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading && !guardarBloqueado,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(Modifier.height(12.dp))

            // ===== Mensajes =====
            val errorToShow = errorLocal ?: errorApi
            if (errorToShow != null) {
                Text(
                    text = errorToShow,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
            }

            if (mensajeOk != null) {
                Text(
                    text = mensajeOk!!,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
            }

            // ===== Botón Guardar =====
            val guardarEnabled = !loading && !guardarBloqueado

            Button(
                onClick = {
                    errorLocal = null
                    mensajeOk = null

                    val err = validarObligatorios()
                    if (err != null) {
                        errorLocal = err
                        return@Button
                    }

                    // Bloquea ya para evitar doble click
                    guardarBloqueado = true

                    val req = ClienteCreateRequestDto(
                        nombre = nombre.trim(),
                        apellidos = apellidos.trim(),
                        dni = dni.trim(),
                        telefono = telefono.trim().ifEmpty { null },
                        direccion = direccion.trim().ifEmpty { null },
                        cp = cp.trim().ifEmpty { null },
                        poblacion = poblacion.trim().ifEmpty { null },
                        provincia = provincia.trim().ifEmpty { null },
                        fecha_nacimiento = fechaNacimientoIso.ifEmpty { null },
                        correo_electronico = correo.trim().ifEmpty { null },
                        firma_rgpd = 0,
                        activo = 1
                    )

                    vm.crearCliente(req)
                },
                enabled = guardarEnabled,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Guardar")
                }
            }

            // ===== Botón Firma RGPD =====
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { clienteCreadoId?.let { onFirmaRgpd(it) } },
                enabled = !loading && clienteCreadoId != null,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Firma para la aceptación de la RGPD")
            }

            // ===== Botón Menú principal =====
            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onMenuPrincipal,
                enabled = !loading,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Menú principal")
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
