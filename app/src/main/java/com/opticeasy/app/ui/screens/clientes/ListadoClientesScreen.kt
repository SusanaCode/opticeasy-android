package com.opticeasy.app.ui.screens.clientes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.opticeasy.app.data.remote.dto.clientes.ClienteDto
import com.opticeasy.app.ui.components.BaseScreen
import com.opticeasy.app.ui.theme.OpticCardBg
import com.opticeasy.app.ui.theme.OpticSecondary
import com.opticeasy.app.viewmodel.clientes.ClientesBuscarState
import com.opticeasy.app.viewmodel.clientes.ClientesBuscarViewModel

@Composable
fun ListadoClientesScreen(
    vm: ClientesBuscarViewModel,
    onBack: () -> Unit,
    onClienteClick: ((ClienteDto) -> Unit)? = null
) {
    val state by vm.state.collectAsState()

    val clientes = (state as? ClientesBuscarState.Success)?.clientes ?: emptyList()
    val isLoading = state is ClientesBuscarState.Loading
    val errorMsg = (state as? ClientesBuscarState.Error)?.message

    val shape16 = RoundedCornerShape(16.dp)
    val listState = rememberLazyListState()

    BaseScreen(contentTopPadding = 8.dp) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Listado de clientes",
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

                if (errorMsg != null) {
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape16)
                        .padding(vertical = 12.dp, horizontal = 14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HeaderCell("Nombre", 0.18f)
                    HeaderCell("Apellidos", 0.30f)
                    HeaderCell("DNI", 0.18f)
                    HeaderCell("Teléfono", 0.20f)
                    HeaderCell("Código", 0.14f)
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (!isLoading && clientes.isEmpty()) {
                    Text("No se encontraron clientes.")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onBack,
                        shape = shape16,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Atrás")
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(clientes) { c ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onClienteClick?.invoke(c) },
                                shape = shape16,
                                colors = CardDefaults.cardColors(
                                    containerColor = OpticCardBg
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 14.dp, horizontal = 14.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    BodyCell(c.nombre, 0.18f)
                                    BodyCell(c.apellidos, 0.30f)
                                    BodyCell(c.dni ?: "", 0.18f)
                                    BodyCell(c.telefono ?: "", 0.20f)
                                    BodyCell(c.idCliente.toString(), 0.14f)
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = onBack,
                                shape = shape16,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                            ) {
                                Text("Atrás")
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }

            if (isLoading) {
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

@Composable
private fun RowScope.HeaderCell(text: String, weight: Float) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.weight(weight),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun RowScope.BodyCell(text: String, weight: Float) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.weight(weight),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}