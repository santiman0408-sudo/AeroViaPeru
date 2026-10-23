package com.example.aeroviaperu

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aeroviaperu.data.ReservaDbHelper
import com.example.aeroviaperu.models.Reserva
import java.text.NumberFormat
import java.util.Locale
import java.util.UUID

class CrearReservaActivity : AppCompatActivity() {

    private lateinit var edtClienteNombre: EditText
    private lateinit var edtTelefono: EditText
    private lateinit var txtVueloSeleccionado: TextView
    private lateinit var dbHelper: ReservaDbHelper

    private var numeroVuelo = ""
    private var aerolinea = "AeroVía Perú"
    private var origen = ""
    private var destino = ""
    private var fechaViaje = ""
    private var horaSalida = ""
    private var horaLlegada = ""
    private var cantidadPersonas = 1
    private var precioUnitario = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_reserva)

        dbHelper = ReservaDbHelper(this)

        edtClienteNombre = findViewById(R.id.edtClienteNombre)
        edtTelefono = findViewById(R.id.edtTelefono)
        txtVueloSeleccionado = findViewById(R.id.txtVueloSeleccionado)

        numeroVuelo = intent.getStringExtra("numeroVuelo") ?: ""
        aerolinea = intent.getStringExtra("aerolinea")
            ?: "AeroVía Perú"
        origen = intent.getStringExtra("origen") ?: ""
        destino = intent.getStringExtra("destino") ?: ""
        fechaViaje = intent.getStringExtra("fechaViaje") ?: ""
        horaSalida = intent.getStringExtra("horaSalida") ?: ""
        horaLlegada = intent.getStringExtra("horaLlegada") ?: ""

        cantidadPersonas = intent.getIntExtra(
            "cantidadPersonas", 1
        ).coerceIn(1, 9)

        precioUnitario = intent.getDoubleExtra(
            "precioUnitario", 0.0
        )

        mostrarResumenVuelo()

        findViewById<Button>(R.id.btnRegistrarReserva)
            .setOnClickListener {
                registrarReserva()
            }
    }

    private fun mostrarResumenVuelo() {
        val total = precioUnitario * cantidadPersonas

        txtVueloSeleccionado.text = """
            Vuelo: $numeroVuelo
            Aerolínea: $aerolinea
            Ruta: $origen → $destino
            Fecha: $fechaViaje
            Horario: $horaSalida – $horaLlegada
            Pasajeros: $cantidadPersonas
            Total referencial: ${formatoMoneda(total)}
        """.trimIndent()
    }

    private fun registrarReserva() {
        val nombre = edtClienteNombre.text.toString().trim()
        val telefono = edtTelefono.text.toString().trim()

        if (nombre.length < 3) {
            edtClienteNombre.error =
                "Ingresa el nombre del pasajero"
            edtClienteNombre.requestFocus()
            return
        }

        if (!telefono.matches(Regex("[0-9]{9}"))) {
            edtTelefono.error =
                "Ingresa un teléfono de 9 dígitos"
            edtTelefono.requestFocus()
            return
        }

        if (numeroVuelo.isBlank() ||
            origen.isBlank() ||
            destino.isBlank() ||
            fechaViaje.isBlank()
        ) {
            Toast.makeText(
                this,
                "Faltan datos del vuelo. Vuelve a buscar.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val codigo = "AV-" + UUID.randomUUID()
            .toString()
            .take(8)
            .uppercase(Locale.ROOT)

        val reserva = Reserva(
            codigoReserva = codigo,
            clienteNombre = nombre,
            telefono = telefono,
            numeroVuelo = numeroVuelo,
            aerolinea = aerolinea,
            origen = origen,
            destino = destino,
            fechaViaje = fechaViaje,
            horaSalida = horaSalida,
            horaLlegada = horaLlegada,
            cantidadPersonas = cantidadPersonas,
            precioUnitario = precioUnitario,
            precioTotal = precioUnitario * cantidadPersonas,
            estadoReserva = "PENDIENTE",
            puntoEncuentro = "Aeropuerto de $origen"
        )

        val id = try {
            dbHelper.insertarReserva(reserva)
        } catch (e: Exception) {
            -1L
        }

        if (id > 0) {
            Toast.makeText(
                this,
                "Reserva guardada: $codigo",
                Toast.LENGTH_LONG
            ).show()

            finish()
        } else {
            Toast.makeText(
                this,
                "No se pudo guardar la reserva.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun formatoMoneda(importe: Double): String {
        return NumberFormat.getCurrencyInstance(
            Locale("es", "PE")
        ).format(importe)
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}

