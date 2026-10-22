package com.example.app22_jc_room_sync_neon.model.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.app22_jc_room_sync_neon.model.EstadoSync

// Tabla "pedidos" en la base local del celular
@Entity(
    tableName = "pedidos",
    indices = [Index(value = ["uuid"], unique = true)]
)
data class PedidoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val uuid: String,
    val producto: String,
    val cantidad: Int,
    val fechaRegistro: Long,
    val estado: EstadoSync = EstadoSync.PENDIENTE,   // Room guarda el enum como texto
    val idRemoto: Int? = null,
    val mensajeError: String? = null
)
