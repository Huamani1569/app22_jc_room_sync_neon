package com.example.app22_jc_room_sync_neon.model.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // Emulador: 10.0.2.2 es la PC donde corre la API.
    // Celular físico: IP de tu PC en la misma red Wi-Fi, por ejemplo "http://192.168.1.50:3001/"
    // Puerto 3001: así la API de app19 (3000) y esta pueden correr al mismo tiempo
    private const val BASE_URL = "http://10.0.2.2:3001/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY   // Logcat, filtro: okhttp
    }

    // Tiempos cortos: si el servidor no responde, el pedido queda pendiente y se reintenta luego
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    val api: PedidoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PedidoApiService::class.java)
    }
}
