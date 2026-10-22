package com.example.app22_jc_room_sync_neon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.app22_jc_room_sync_neon.ui.theme.App22_jc_room_sync_neonTheme
import com.example.app22_jc_room_sync_neon.view.PantallaPedidos

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Revisa el nombre real del tema en ui/theme/Theme.kt
            App22_jc_room_sync_neonTheme {
                PantallaPedidos()
            }
        }
    }
}
