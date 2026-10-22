package com.example.app22_jc_room_sync_neon.model

import com.example.app22_jc_room_sync_neon.model.local.PedidoEntity
import com.example.app22_jc_room_sync_neon.model.remote.PedidoDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// Room → modelo de la app
fun PedidoEntity.aModelo() = Pedido(
    id = id,
    uuid = uuid,
    producto = producto,
    cantidad = cantidad,
    fechaRegistro = fechaRegistro,
    estado = estado,
    idRemoto = idRemoto,
    mensajeError = mensajeError
)

// Room → JSON para la API
fun PedidoEntity.aDto() = PedidoDto(
    uuid = uuid,
    producto = producto,
    cantidad = cantidad,
    fechaRegistro = fechaIsoUtc(fechaRegistro)
)

private fun fechaIsoUtc(millis: Long): String =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }.format(Date(millis))
