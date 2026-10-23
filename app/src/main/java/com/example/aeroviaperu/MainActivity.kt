package com.example.aeroviaperu

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AeroViaPeru"
        private const val KEY_FECHA = "fecha_seleccionada"
    }

    private lateinit var spinnerOrigen: Spinner
    private lateinit var spinnerDestino: Spinner
    private lateinit var spinnerPasajeros: Spinner

    private lateinit var btnFecha: Button
    private lateinit var btnBuscarVuelos: Button
    private lateinit var btnMisReservas: Button

    private var fechaSeleccionada: String = ""

    private val ciudades = listOf(
        "Lima",
        "Cusco",
        "Arequipa",
        "Trujillo",
        "Piura",
        "Iquitos",
        "Tarapoto",
        "Chiclayo",
        "Pucallpa",
        "Tacna"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "Iniciando MainActivity")

        try {
            // Cargar la interfaz original.
            setContentView(R.layout.activity_main)

            Log.d(TAG, "activity_main.xml cargado correctamente")

            // Recuperar los controles del XML.
            spinnerOrigen = encontrarControl(R.id.spinnerOrigen)
            spinnerDestino = encontrarControl(R.id.spinnerDestino)
            spinnerPasajeros = encontrarControl(R.id.spinnerPasajeros)

            btnFecha = encontrarControl(R.id.btnFecha)
            btnBuscarVuelos = encontrarControl(R.id.btnBuscarVuelos)
            btnMisReservas = encontrarControl(R.id.btnMisReservas)

            // Configurar las listas desplegables.
            configurarSpinnerCiudades()
            configurarSpinnerPasajeros()

            // Recuperar la fecha seleccionada.
            fechaSeleccionada =
                savedInstanceState?.getString(KEY_FECHA).orEmpty()

            btnFecha.text = if (fechaSeleccionada.isNotBlank()) {
                convertirFechaParaMostrar(fechaSeleccionada)
            } else {
                "Seleccionar fecha"
            }

            // Abrir el selector de fecha.
            btnFecha.setOnClickListener {
                mostrarSelectorFecha()
            }

            // Buscar vuelos.
            btnBuscarVuelos.setOnClickListener {
                buscarVuelos()
            }

            // Abrir las reservas guardadas.
            btnMisReservas.setOnClickListener {
                abrirMisReservas()
            }

            Log.d(TAG, "Formulario inicializado correctamente")

            // Diagnóstico temporal: comprobar las dimensiones de la interfaz.
            window.decorView.post {
                val contenido = findViewById<View>(android.R.id.content)
                val origen = findViewById<View>(R.id.spinnerOrigen)

                Log.d(
                    TAG,
                    "Contenido: ${contenido.width} x ${contenido.height}"
                )

                Log.d(
                    TAG,
                    "spinnerOrigen: ${origen.width} x ${origen.height}, " +
                            "visible=${origen.visibility == View.VISIBLE}"
                )
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error al inicializar MainActivity", e)

            Toast.makeText(
                this,
                "Error al cargar la pantalla. Revisa Logcat.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun <T : View> encontrarControl(id: Int): T {
        return requireNotNull(findViewById<T>(id)) {
            "No se encontró la vista con ID " +
                    "${resources.getResourceEntryName(id)} " +
                    "en activity_main.xml"
        }
    }

    private fun configurarSpinnerCiudades() {
        val adaptadorOrigen = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ciudades
        )

        adaptadorOrigen.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerOrigen.adapter = adaptadorOrigen
        spinnerOrigen.setSelection(0)

        val adaptadorDestino = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ciudades
        )

        adaptadorDestino.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinnerDestino.adapter = adaptadorDestino
        spinnerDestino.setSelection(1)
    }

    private fun configurarSpinnerPasajeros() {
        val cantidades = (1..6).map { cantidad ->
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

        spinnerPasajeros.adapter = adaptador
        spinnerPasajeros.setSelection(1)
    }

    private fun mostrarSelectorFecha() {
        val calendario = Calendar.getInstance()

        if (fechaSeleccionada.isNotBlank()) {
            try {
                val formato = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.US
                )

                formato.isLenient = false

                formato.parse(fechaSeleccionada)?.let {
                    calendario.time = it
                }
            } catch (e: Exception) {
                Log.e(TAG, "No se pudo recuperar la fecha", e)
            }
        }

        val selector = DatePickerDialog(
            this,
            { _, anio, mes, dia ->
                fechaSeleccionada = String.format(
                    Locale.US,
                    "%04d-%02d-%02d",
                    anio,
                    mes + 1,
                    dia
                )

                btnFecha.text =
                    convertirFechaParaMostrar(fechaSeleccionada)

                Log.d(TAG, "Fecha seleccionada: $fechaSeleccionada")
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        )

        // Impedir la selección de fechas anteriores a hoy.
        val hoy = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        selector.datePicker.minDate = hoy.timeInMillis
        selector.show()
    }

    private fun convertirFechaParaMostrar(fecha: String): String {
        return try {
            val formatoEntrada = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.US
            ).apply {
                isLenient = false
            }

            val formatoSalida = SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            )

            val fechaConvertida = formatoEntrada.parse(fecha)

            fechaConvertida?.let {
                formatoSalida.format(it)
            } ?: "Seleccionar fecha"

        } catch (e: Exception) {
            Log.e(TAG, "Error al convertir la fecha", e)
            "Seleccionar fecha"
        }
    }

    private fun buscarVuelos() {
        val origen = spinnerOrigen.selectedItem?.toString().orEmpty()
        val destino = spinnerDestino.selectedItem?.toString().orEmpty()
        val pasajerosTexto =
            spinnerPasajeros.selectedItem?.toString().orEmpty()

        if (origen.isBlank() || destino.isBlank()) {
            mostrarMensaje("Selecciona una ciudad de origen y destino.")
            return
        }

        if (origen.equals(destino, ignoreCase = true)) {
            mostrarMensaje("La ciudad de origen debe ser diferente al destino.")
            return
        }

        if (fechaSeleccionada.isBlank()) {
            mostrarMensaje("Selecciona la fecha de salida.")
            return
        }

        val cantidadPersonas = pasajerosTexto
            .substringBefore(" ")
            .toIntOrNull()

        if (cantidadPersonas == null || cantidadPersonas !in 1..6) {
            mostrarMensaje("Selecciona una cantidad válida de pasajeros.")
            return
        }

        val intent = Intent(this, VuelosActivity::class.java).apply {
            putExtra("origen", origen)
            putExtra("destino", destino)
            putExtra("fechaViaje", fechaSeleccionada)
            putExtra("cantidadPersonas", cantidadPersonas)
        }

        startActivity(intent)
    }

    private fun abrirMisReservas() {
        startActivity(
            Intent(this, MisReservasActivity::class.java)
        )
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(
            this,
            mensaje,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_FECHA, fechaSeleccionada)
        super.onSaveInstanceState(outState)
    }
}

