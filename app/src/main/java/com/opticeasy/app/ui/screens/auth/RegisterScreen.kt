package com.opticeasy.app.ui.screens.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.opticeasy.app.ui.components.BaseScreen
import com.opticeasy.app.ui.theme.OpticOnPrimary
import com.opticeasy.app.ui.theme.OpticPrimary
import com.opticeasy.app.viewmodel.auth.AuthUiState
import com.opticeasy.app.viewmodel.auth.AuthViewModel


private fun esPasswordValida(password: String): Boolean {
    val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")
    return regex.matches(password)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit,
    vm: AuthViewModel = viewModel()
) {

    val context = LocalContext.current
    val state by vm.state.collectAsState()
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellidos by rememberSaveable { mutableStateOf("") }
    var nick by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var codigoCentro by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var rol by rememberSaveable { mutableStateOf("optico") }
    var expandedRol by rememberSaveable { mutableStateOf(false) }
    var numeroColegiado by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state) {
        when (state) {

            is AuthUiState.Success -> {
                Toast.makeText(
                    context,
                    "Usuario creado correctamente",
                    Toast.LENGTH_SHORT
                ).show()

                vm.resetState()
                onRegisterSuccess()
            }

            is AuthUiState.Error -> {
                Toast.makeText(
                    context,
                    (state as AuthUiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()

                vm.resetState()
            }

            else -> Unit
        }
    }

    BaseScreen {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Registro nuevo usuario",
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

                CampoTexto(nombre, { nombre = it }, "Nombre")

                Spacer(modifier = Modifier.height(16.dp))

                CampoTexto(apellidos, { apellidos = it }, "Apellidos")

                Spacer(modifier = Modifier.height(16.dp))

                CampoTexto(nick, { nick = it }, "Nick")

                Spacer(modifier = Modifier.height(16.dp))

                CampoTexto(email, { email = it }, "Email", KeyboardType.Email)

                Spacer(modifier = Modifier.height(16.dp))

                CampoTexto(codigoCentro, { codigoCentro = it }, "Código centro")

                Spacer(modifier = Modifier.height(16.dp))


                ExposedDropdownMenuBox(
                    expanded = expandedRol,
                    onExpandedChange = { expandedRol = !expandedRol },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    val rolLabel = if (rol == "optico") "Óptico" else "Comercial"

                    OutlinedTextField(
                        value = rolLabel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Rol") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRol)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = coloresCampo()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedRol,
                        onDismissRequest = { expandedRol = false }
                    ) {

                        DropdownMenuItem(
                            text = { Text("Óptico") },
                            onClick = {
                                rol = "optico"
                                expandedRol = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Comercial") },
                            onClick = {
                                rol = "comercial"
                                numeroColegiado = ""
                                expandedRol = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = numeroColegiado,
                    onValueChange = { numeroColegiado = it.filter(Char::isDigit) },
                    label = { Text("Nº colegiado (solo ópticos)") },
                    enabled = rol == "optico",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = coloresCampo()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation =
                        if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {

                        val image =
                            if (passwordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff

                        IconButton(
                            onClick = { passwordVisible = !passwordVisible }
                        ) {
                            Icon(
                                imageVector = image,
                                contentDescription =
                                    if (passwordVisible)
                                        "Ocultar contraseña"
                                    else
                                        "Mostrar contraseña"
                            )
                        }
                    },
                    colors = coloresCampo()
                )

                Text(
                    text = "Debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedButton(
                    onClick = {
                        nombre = ""
                        apellidos = ""
                        nick = ""
                        email = ""
                        codigoCentro = ""
                        password = ""
                        rol = "optico"
                        numeroColegiado = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Limpiar")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val passwordLimpia = password.trim()

                        if (!esPasswordValida(passwordLimpia)) {
                            Toast.makeText(
                                context,
                                "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número",
                                Toast.LENGTH_LONG
                            ).show()
                            return@Button
                        }

                        vm.register(
                            nombreUsuario = nombre.trim(),
                            apellidosUsuario = apellidos.trim(),
                            nickUsuario = nick.trim(),
                            email = email.trim(),
                            password = passwordLimpia,
                            codigoCentro = codigoCentro.trim(),
                            rol = rol,
                            numeroColegiado = numeroColegiado.ifBlank { null }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OpticPrimary,
                        contentColor = OpticOnPrimary
                    )
                ) {
                    Text("Enviar")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OpticPrimary,
                        contentColor = OpticOnPrimary
                    )
                ) {
                    Text("Atrás")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun CampoTexto(
    valor: String,
    onChange: (String) -> Unit,
    label: String,
    keyboard: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboard),
        modifier = Modifier.fillMaxWidth(),
        colors = coloresCampo()
    )
}

@Composable
private fun coloresCampo() =
    OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface
    )