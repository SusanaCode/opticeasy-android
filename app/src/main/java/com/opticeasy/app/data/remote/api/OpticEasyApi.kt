package com.opticeasy.app.data.remote.api

import com.opticeasy.app.data.remote.dto.clientes.ClienteCreateRequestDto
import com.opticeasy.app.data.remote.dto.clientes.ClienteCreateResponseDto
import com.opticeasy.app.data.remote.dto.clientes.ClienteDto
import com.opticeasy.app.data.remote.dto.clientes.ClienteUpdateRequestDto
import com.opticeasy.app.data.remote.dto.rgpd.FirmaRgpdRequestDto
import com.opticeasy.app.data.remote.dto.auth.LoginRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.opticeasy.app.data.remote.dto.common.OkResponseDto
import com.opticeasy.app.data.remote.dto.auth.RegisterRequestDto
import com.opticeasy.app.data.remote.dto.revisiones_gafa.RevisionGafaCreateRequestDto
import com.opticeasy.app.data.remote.dto.revisiones_gafa.RevisionGafaCreateResponseDto
import com.opticeasy.app.data.remote.dto.revisiones_gafa.RevisionGafaListItemDto
import com.opticeasy.app.data.remote.dto.Revisiones_lc.RevisionLcCreateRequestDto
import com.opticeasy.app.data.remote.dto.Revisiones_lc.RevisionLcCreateResponseDto
import com.opticeasy.app.data.remote.dto.Revisiones_lc.RevisionLcListItemDto
import com.opticeasy.app.data.remote.dto.auth.LoginResponseDto
import com.opticeasy.app.data.remote.dto.auth.UsuarioDto
import com.opticeasy.app.data.remote.dto.usuarios.CambiarPasswordRequestDto
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Query
import com.opticeasy.app.data.remote.dto.usuarios.CambiarActivoRequestDto


interface OpticEasyApi {

    @GET("clientes")
    suspend fun getClientes(): List<ClienteDto>

    @POST("clientes")
    suspend fun crearCliente(
        @Body body: ClienteCreateRequestDto
    ): ClienteCreateResponseDto

    @POST("/clientes/{id}/firmas")
    suspend fun crearFirmaRgpd(
        @Path("id") idCliente: Int,
        @Body body: FirmaRgpdRequestDto
    ): OkResponseDto

    @POST("clientes/{id}/revisiones-gafa")
    suspend fun crearRevisionGafa(
        @Path("id") clienteId: Long,
        @Body body: RevisionGafaCreateRequestDto
    ): RevisionGafaCreateResponseDto

    @POST("clientes/{id}/revisiones-lc")
    suspend fun crearRevisionLc(
        @Path("id") clienteId: Long,
        @Body body: RevisionLcCreateRequestDto
    ): RevisionLcCreateResponseDto

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): LoginResponseDto

    @GET("clientes/buscar")
    suspend fun buscarClientes(
        @Query("nombre") nombre: String?,
        @Query("apellidos") apellidos: String?,
        @Query("dni") dni: String?,
        @Query("telefono") telefono: String?
    ): List<ClienteDto>

    @GET("clientes/{id}")
    suspend fun obtenerClientePorId(
        @Path("id") id: Int
    ): ClienteDto

    @PUT("clientes/{id}")
    suspend fun actualizarCliente(
        @Path("id") id: Int,
        @Body body: ClienteUpdateRequestDto
    ): OkResponseDto

    @POST("usuarios")
    suspend fun register(
        @Body body: RegisterRequestDto
    ): UsuarioDto

    @GET("clientes/{id}/revisiones-gafa")
    suspend fun listarRevisionesGafa(
        @Path("id") clienteId: Long
    ): List<RevisionGafaListItemDto>

    @GET("clientes/{id}/revisiones-lc")
    suspend fun listarRevisionesLc(
        @Path("id") clienteId: Long
    ): List<RevisionLcListItemDto>

    @GET("usuarios")
    suspend fun listarUsuarios(): List<UsuarioDto>

    @PATCH("usuarios/{id}/password")
    suspend fun cambiarPasswordUsuario(
        @Path("id") id: Int,
        @Body body: CambiarPasswordRequestDto
    ): OkResponseDto

    @PATCH("usuarios/{id}/activo")
    suspend fun cambiarActivoUsuario(
        @Path("id") id: Int,
        @Body body: CambiarActivoRequestDto
    ): UsuarioDto
}



