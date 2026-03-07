package com.opticeasy.app.data.repository

import com.opticeasy.app.core.network.RetrofitClient
import com.opticeasy.app.data.remote.dto.clientes.ClienteCreateRequestDto
import com.opticeasy.app.data.remote.dto.clientes.ClienteCreateResponseDto
import com.opticeasy.app.data.remote.dto.clientes.ClienteDto
import com.opticeasy.app.data.remote.dto.clientes.ClienteUpdateRequestDto
import com.opticeasy.app.data.remote.dto.common.OkResponseDto

class ClientesRepository {


    suspend fun crearCliente(req: ClienteCreateRequestDto): ClienteCreateResponseDto {
        return RetrofitClient.api.crearCliente(req)
    }


    suspend fun buscarClientes(
        nombre: String?,
        apellidos: String?,
        dni: String?,
        telefono: String?
    ): List<ClienteDto> {
        return RetrofitClient.api.buscarClientes(
            nombre = nombre?.takeIf { it.isNotBlank() },
            apellidos = apellidos?.takeIf { it.isNotBlank() },
            dni = dni?.takeIf { it.isNotBlank() },
            telefono = telefono?.takeIf { it.isNotBlank() }
        )
    }


    suspend fun obtenerClientePorId(idCliente: Int): ClienteDto {
        return RetrofitClient.api.obtenerClientePorId(idCliente)
    }

    suspend fun actualizarCliente(idCliente: Int, req: ClienteUpdateRequestDto): OkResponseDto {
        return RetrofitClient.api.actualizarCliente(idCliente, req)
    }
}