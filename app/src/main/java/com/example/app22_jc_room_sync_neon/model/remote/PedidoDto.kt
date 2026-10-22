package com.example.app22_jc_room_sync_neon.model.remote

import com.google.gson.annotations.SerializedName

// Lo que viaja a la API (JSON)
data class PedidoDto(
    val uuid: String,
    val producto: String,
    val cantidad: Int,
    @SerializedName("fecha_registro")
    val fechaRegistro: String     // ISO 8601 en UTC, por ejemplo "2026-10-21T22:15:03.120Z"
)

// Lo que responde la API
data class RespuestaPedido(
    val id: Int,
    val uuid: String
)
