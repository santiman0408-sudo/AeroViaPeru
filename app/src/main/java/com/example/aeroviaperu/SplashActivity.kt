package com.example.aeroviaperu

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Mostrar el diseño XML de bienvenida.
        setContentView(R.layout.activity_splash)

        // 2. Configurar los colores de las barras del sistema.
        window.statusBarColor = Color.rgb(234, 244, 255)
        window.navigationBarColor = Color.rgb(216, 232, 250)

        // 3. Obtener el botón Comenzar del XML.
        val btnComenzar = findViewById<Button>(R.id.btnComenzar)

        // 4. Definir la acción al pulsar el botón.
        btnComenzar.setOnClickListener {

            // Abrir la pantalla principal existente.
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Cerrar la bienvenida para no regresar a ella con Atrás.
            finish()
        }
    }
}
