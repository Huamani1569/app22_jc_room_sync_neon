package com.example.app22_jc_room_sync_neon.model

import com.example.app22_jc_room_sync_neon.model.local.PedidoDao
import com.example.app22_jc_room_sync_neon.model.local.PedidoEntity
import com.example.app22_jc_room_sync_neon.model.remote.PedidoApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import java.util.UUID

data class ResultadoSync(val enviados: Int, val fallidos: Int)

// Coordina Room (siempre) y la API (solo al sincronizar)
class PedidoRepository(
    private val dao: PedidoDao,
    private val api: PedidoApiService
) {

    // La pantalla lee siempre de Room, haya o no conexión
    val pedidos: Flow<List<Pedido>> =
        dao.observarTodos().map { lista -> lista.map { it.aModelo() } }

    // Offline-first: el pedido se guarda en el celular, con o sin Internet
    suspend fun registrar(producto: String, cantidad: Int) {
        dao.insertar(
            PedidoEntity(
                uuid = UUID.randomUUID().toString(),
                producto = producto,
                cantidad = cantidad,
                fechaRegistro = System.currentTimeMillis()
            )
        )
    }

    // Envía a Neon los pedidos PENDIENTE o ERROR, uno por uno
    suspend fun sincronizar(): ResultadoSync {
        var enviados = 0
        var fallidos = 0

        for (pedido in dao.obtenerNoSincronizados()) {
            try {
                val respuesta = api.enviar(pedido.aDto())
                dao.marcarSincronizado(pedido.id, respuesta.id)
                enviados++
            } catch (e: HttpException) {
                // El servidor respondió con error (400, 500...): se marca y se sigue con el siguiente
                dao.marcarError(pedido.id, "El servidor respondió ${e.code()}")
                fallidos++
            } catch (e: IOException) {
                // No hubo respuesta: el resto queda pendiente para el próximo intento
                dao.marcarError(pedido.id, "Sin respuesta del servidor")
                fallidos++
                break
            }
        }
        return ResultadoSync(enviados, fallidos)
    }
}
