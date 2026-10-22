package com.example.app22_jc_room_sync_neon.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {

    // Flow: la lista en pantalla se actualiza sola cuando cambia la tabla
    @Query("SELECT * FROM pedidos ORDER BY fechaRegistro DESC")
    fun observarTodos(): Flow<List<PedidoEntity>>

    // PENDIENTE y ERROR se vuelven a intentar en cada sincronización
    @Query("SELECT * FROM pedidos WHERE estado != 'SINCRONIZADO' ORDER BY fechaRegistro")
    suspend fun obtenerNoSincronizados(): List<PedidoEntity>

    @Insert
    suspend fun insertar(pedido: PedidoEntity): Long

    @Query("UPDATE pedidos SET estado = 'SINCRONIZADO', idRemoto = :idRemoto, mensajeError = NULL WHERE id = :id")
    suspend fun marcarSincronizado(id: Long, idRemoto: Int)

    @Query("UPDATE pedidos SET estado = 'ERROR', mensajeError = :mensaje WHERE id = :id")
    suspend fun marcarError(id: Long, mensaje: String)
}
