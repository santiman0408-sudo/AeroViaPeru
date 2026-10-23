package com.example.aeroviaperu

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.Locale

class DetalleReservaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_reserva)

        val txtCodigo =
            findViewById<TextView>(R.id.txtDetalleCodigo)

        val txtCliente =
            findViewById<TextView>(R.id.txtDetalleCliente)

        val txtTelefono =
            findViewById<TextView>(R.id.txtDetalleTelefono)

        val txtRuta =
            findViewById<TextView>(R.id.txtDetalleRuta)

        val txtAerolinea =
            findViewById<TextView>(R.id.txtDetalleAerolinea)

        val txtNumeroVuelo =
            findViewById<TextView>(R.id.txtDetalleNumeroVuelo)

        val txtFecha =
            findViewById<TextView>(R.id.txtDetalleFecha)

        val txtHorario =
            findViewById<TextView>(R.id.txtDetalleHorario)

        val txtPasajeros =
            findViewById<TextView>(R.id.txtDetallePasajeros)

        val txtPrecio =
            findViewById<TextView>(R.id.txtDetallePrecio)

        val txtEstado =
            findViewById<TextView>(R.id.txtDetalleEstado)

        // Recuperar datos enviados desde la pantalla anterior
        val codigo = intent.getStringExtra("codigoReserva").orEmpty()
        val cliente = intent.getStringExtra("clienteNombre").orEmpty()
        val telefono = intent.getStringExtra("telefono").orEmpty()
        val origen = intent.getStringExtra("origen").orEmpty()
        val destino = intent.getStringExtra("destino").orEmpty()
        val aerolinea = intent.getStringExtra("aerolinea").orEmpty()
        val numeroVuelo = intent.getStringExtra("numeroVuelo").orEmpty()
        val fecha = intent.getStringExtra("fechaViaje").orEmpty()
        val horaSalida = intent.getStringExtra("horaSalida").orEmpty()
        val horaLlegada = intent.getStringExtra("horaLlegada").orEmpty()
        val cantidad = intent.getIntExtra("cantidadPersonas", 1)
        val precio = intent.getDoubleExtra("precioTotal", 0.0)
        val estado = intent.getStringExtra("estadoReserva")
            ?: "PENDIENTE"

        // Mostrar información
        txtCodigo.text = "Reserva $codigo"
        txtCliente.text = "Pasajero: $cliente"
        txtTelefono.text = "Teléfono: $telefono"
        txtRuta.text = "$origen → $destino"
        txtAerolinea.text = "Aerolínea: $aerolinea"
        txtNumeroVuelo.text = "Número de vuelo: $numeroVuelo"
        txtFecha.text = "Fecha de viaje: $fecha"
        txtHorario.text = "Horario: $horaSalida – $horaLlegada"
        txtPasajeros.text = "Pasajeros: $cantidad"

        txtPrecio.text = NumberFormat.getCurrencyInstance(
            Locale("es", "PE")
        ).format(precio)

        txtEstado.text = estado

        // Volver a la pantalla anterior
        findViewById<Button>(R.id.btnVolverDetalle)
            .setOnClickListener {
                finish()
            }
    }
}
