package com.example.aeroviaperu

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.aeroviaperu.data.ReservaDbHelper
import com.example.aeroviaperu.models.Reserva
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

class EditarReservaActivity : AppCompatActivity() {

    private lateinit var dbHelper: ReservaDbHelper

    private lateinit var txtCodigo: TextView
    private lateinit var edtNombre: EditText
    private lateinit var edtTelefono: EditText
    private lateinit var edtFecha: EditText
    private lateinit var spnCantidad: Spinner
    private lateinit var txtPrecioTotal: TextView
    private lateinit var btnGuardar: Button
    private lateinit var btnCancelar: Button

    private var reservaOriginal: Reserva? = null
    private var precioUnitario: Double = 0.0
    private var inicializandoSpinner = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_reserva)

        dbHelper = ReservaDbHelper(this)

        txtCodigo = findViewById(R.id.txtCodigoEditar)
        edtNombre = findViewById(R.id.edtNombreEditar)
        edtTelefono = findViewById(R.id.edtTelefonoEditar)
        edtFecha = findViewById(R.id.edtFechaEditar)
        spnCantidad = findViewById(R.id.spnCantidadEditar)
        txtPrecioTotal = findViewById(R.id.txtPrecioTotalEditar)
        btnGuardar = findViewById(R.id.btnGuardarCambios)
        btnCancelar = findViewById(R.id.btnCancelarEdicion)

        configurarSpinner()

        edtFecha.apply {
            isFocusable = false
            isClickable = true
            isLongClickable = false
            keyListener = null
        }

        edtFecha.setOnClickListener {
            mostrarSelectorFecha()
        }

        val reservaId = intent.getLongExtra("reserva_id", -1L)

        if (reservaId <= 0L) {
            mostrarMensaje("No se recibió un ID de reserva válido")
            finish()
            return
        }

        // Recupera la reserva guardada en SQLite.
        reservaOriginal = dbHelper.obtenerReservas()
            .firstOrNull { it.id == reservaId }

        val reserva = reservaOriginal

        if (reserva == null) {
            mostrarMensaje("No se encontró la reserva")
            finish()
            return
        }

        // Conserva la tarifa unitaria de la reserva.
        precioUnitario = reserva.precioUnitario

        // Carga los datos actuales en el formulario.
        txtCodigo.text = "Reserva ${reserva.codigoReserva}"
        edtNombre.setText(reserva.clienteNombre)
        edtTelefono.setText(reserva.telefono)
        edtFecha.setText(reserva.fechaViaje)

        val posicion = (reserva.cantidadPersonas - 1)
            .coerceIn(0, spnCantidad.count - 1)

        spnCantidad.setSelection(posicion)

        actualizarPrecio(reserva.cantidadPersonas)

        inicializandoSpinner = false

        spnCantidad.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (!inicializandoSpinner) {
                        actualizarPrecio(position + 1)
                    }
                }

                override fun onNothingSelected(
                    parent: AdapterView<*>?
                ) {
                    // No hay ninguna opción seleccionada.
                }
            }

        btnGuardar.setOnClickListener {
            guardarCambios()
        }

        btnCancelar.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun configurarSpinner() {
        val cantidades = (1..9).map { cantidad ->
            if (cantidad == 1) {
                "1 pasajero"
            } else {
                "$cantidad pasajeros"
            }
        }

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            cantidades
        )

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spnCantidad.adapter = adaptador
    }

    private fun mostrarSelectorFecha() {
        val fechaActual = edtFecha.text.toString().trim()
        val partes = fechaActual.split("-")

        val calendario = Calendar.getInstance()

        if (partes.size == 3) {
            val anio = partes[0].toIntOrNull()
            val mes = partes[1].toIntOrNull()
            val dia = partes[2].toIntOrNull()

            if (anio != null && mes != null && dia != null &&
                mes in 1..12 && dia in 1..31
            ) {
                calendario.set(
                    anio,
                    mes - 1,
                    dia,
                    12,
                    0,
                    0
                )
            }
        }

        DatePickerDialog(
            this,
            { _, anio, mes, dia ->
                val fechaSeleccionada = String.format(
                    Locale.US,
                    "%04d-%02d-%02d",
                    anio,
                    mes + 1,
                    dia
                )

                edtFecha.setText(fechaSeleccionada)
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun actualizarPrecio(cantidad: Int) {
        val total = precioUnitario * cantidad

        val formatoMoneda = NumberFormat.getCurrencyInstance(
            Locale("es", "PE")
        )

        txtPrecioTotal.text = "Total: ${formatoMoneda.format(total)}"
    }

    private fun guardarCambios() {
        val reserva = reservaOriginal ?: run {
            mostrarMensaje("No se encontró la reserva que deseas editar")
            return
        }

        val nombre = edtNombre.text.toString().trim()
        val telefono = edtTelefono.text.toString().trim()
        val fecha = edtFecha.text.toString().trim()

        if (nombre.isBlank()) {
            edtNombre.error = "Ingresa el nombre del pasajero"
            edtNombre.requestFocus()
            return
        }

        if (telefono.isBlank()) {
            edtTelefono.error = "Ingresa el teléfono de contacto"
            edtTelefono.requestFocus()
            return
        }

        if (!fecha.matches(Regex("""\d{4}-\d{2}-\d{2}""")) ||
            !esFechaValida(fecha)
        ) {
            mostrarMensaje("Selecciona una fecha válida")
            return
        }

        val cantidad = spnCantidad.selectedItemPosition + 1

        if (cantidad !in 1..9) {
            mostrarMensaje("Selecciona entre 1 y 9 pasajeros")
            return
        }

        val reservaActualizada = reserva.copy(
            clienteNombre = nombre,
            telefono = telefono,
            fechaViaje = fecha,
            cantidadPersonas = cantidad,
            precioTotal = precioUnitario * cantidad
        )

        val filasActualizadas = try {
            dbHelper.actualizarReserva(reservaActualizada)
        } catch (e: Exception) {
            mostrarMensaje("Error al guardar: ${e.localizedMessage ?: "desconocido"}")
            return
        }

        if (filasActualizadas > 0) {
            mostrarMensaje("Reserva actualizada correctamente")
            setResult(RESULT_OK)
            finish()
        } else {
            mostrarMensaje("No se pudieron guardar los cambios")
        }
    }

    private fun esFechaValida(fecha: String): Boolean {
        return try {
            val partes = fecha.split("-")
            val anio = partes[0].toInt()
            val mes = partes[1].toInt()
            val dia = partes[2].toInt()

            val calendario = Calendar.getInstance()
            calendario.isLenient = false
            calendario.set(anio, mes - 1, dia, 12, 0, 0)
            calendario.getTime()
            true
        } catch (_: Exception) {
            false
        }
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        if (::dbHelper.isInitialized) {
            dbHelper.close()
        }
        super.onDestroy()
    }
}
