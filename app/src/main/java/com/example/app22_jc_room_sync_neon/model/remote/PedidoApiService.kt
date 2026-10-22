package com.example.app22_jc_room_sync_neon.model.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface PedidoApiService {

    @POST("api/pedidos")
    suspend fun enviar(@Body pedido: PedidoDto): RespuestaPedido
}
