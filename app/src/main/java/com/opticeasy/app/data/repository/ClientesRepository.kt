package com.opticeasy.app.data.repository

import android.content.Context
import com.opticeasy.app.core.network.RetrofitClient
import com.opticeasy.app.data.remote.dto.clientes.ClienteCreateRequestDto
import com.opticeasy.app.data.remote.dto.clientes.ClienteCreateResponseDto
import com.opticeasy.app.data.remote.dto.clientes.ClienteDto
import com.opticeasy.app.data.remote.dto.clientes.ClienteUpdateRequestDto
import com.opticeasy.app.data.remote.dto.common.OkResponseDto

class ClientesRepository(
    context: Context
) {

    private val api = RetrofitClient.getApi(context)

    suspend fun crearCliente(req: ClienteCreateRequestDto): ClienteCreateResponseDto {
        return api.crearCliente(req)
    }

    suspend fun buscarClientes(
        nombre: String?,
        apellidos: String?,
        dni: String?,
        telefono: String?
    ): List<ClienteDto> {
        return api.buscarClientes(
            nombre = nombre?.takeIf { it.isNotBlank() },
            apellidos = apellidos?.takeIf { it.isNotBlank() },
            dni = dni?.takeIf { it.isNotBlank() },
            telefono = telefono?.takeIf { it.isNotBlank() }
        )
    }

    suspend fun obtenerClientePorId(idCliente: Int): ClienteDto {
        return api.obtenerClientePorId(idCliente)
    }

    suspend fun actualizarCliente(idCliente: Int, req: ClienteUpdateRequestDto): OkResponseDto {
        return api.actualizarCliente(idCliente, req)
    }
}