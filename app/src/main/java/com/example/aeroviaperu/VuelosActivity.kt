package com.example.aeroviaperu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aeroviaperu.models.Vuelo
import java.text.NumberFormat
import java.util.Locale

class VuelosActivity : AppCompatActivity() {

    private lateinit var recyclerVuelos: RecyclerView
    private lateinit var txtResumenBusqueda: TextView

    private var origen: String = ""
    private var destino: String = ""
    private var fechaViaje: String = ""
    private var cantidadPersonas: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vuelos)

        // Recuperar los criterios enviados desde MainActivity
        origen = intent.getStringExtra("origen") ?: "Lima"
        destino = intent.getStringExtra("destino") ?: "Cusco"
        fechaViaje = intent.getStringExtra("fechaViaje") ?: ""
        cantidadPersonas = intent.getIntExtra(
            "cantidadPersonas", 1
        ).coerceIn(1, 9)

        // Vincular los componentes del XML
        recyclerVuelos = findViewById(R.id.recyclerVuelos)
        txtResumenBusqueda = findViewById(R.id.txtResumenBusqueda)

        // Mostrar el resumen de búsqueda
        txtResumenBusqueda.text =
            "$origen → $destino\n" +
                    "Fecha: $fechaViaje · Pasajeros: $cantidadPersonas"

        // Configurar la lista vertical
        recyclerVuelos.layoutManager = LinearLayoutManager(this)

        // Vuelos ficticios para la demostración académica
        val vuelos = listOf(
            Vuelo(
                numeroVuelo = "AV101",
                aerolinea = "AeroVía Perú",
                origen = origen,
                destino = destino,
                horaSalida = "08:00",
                horaLlegada = "09:20",
                precio = 189.00
            ),
            Vuelo(
                numeroVuelo = "AV205",
                aerolinea = "AeroVía Perú",
                origen = origen,
                destino = destino,
                horaSalida = "12:30",
                horaLlegada = "13:50",
                precio = 229.00
            ),
            Vuelo(
                numeroVuelo = "AV310",
                aerolinea = "AeroVía Perú",
                origen = origen,
                destino = destino,
                horaSalida = "18:00",
                horaLlegada = "19:20",
                precio = 259.00
            )
        )

        // Conectar los vuelos con RecyclerView
        recyclerVuelos.adapter = VueloAdapter(
            vuelos = vuelos,
            cantidadPersonas = cantidadPersonas,
            onSeleccionar = { vuelo ->
                abrirFormularioReserva(vuelo)
            }
        )
    }

    /**
     * Abre el formulario y envía los datos del vuelo seleccionado.
     */
    private fun abrirFormularioReserva(vuelo: Vuelo) {
        val intent = Intent(
            this,
            CrearReservaActivity::class.java
        ).apply {
            putExtra("numeroVuelo", vuelo.numeroVuelo)
            putExtra("aerolinea", vuelo.aerolinea)
            putExtra("origen", vuelo.origen)
            putExtra("destino", vuelo.destino)
            putExtra("fechaViaje", fechaViaje)
            putExtra("horaSalida", vuelo.horaSalida)
            putExtra("horaLlegada", vuelo.horaLlegada)
            putExtra("cantidadPersonas", cantidadPersonas)
            putExtra("precioUnitario", vuelo.precio)
        }

        startActivity(intent)
    }

    /**
     * Formatea importes en moneda peruana.
     */
    private fun formatoPrecio(precio: Double): String {
        return NumberFormat.getCurrencyInstance(
            Locale("es", "PE")
        ).format(precio)
    }

    /**
     * Adaptador encargado de dibujar cada tarjeta de vuelo.
     */
    private class VueloAdapter(
        private val vuelos: List<Vuelo>,
        private val cantidadPersonas: Int,
        private val onSeleccionar: (Vuelo) -> Unit
    ) : RecyclerView.Adapter<VueloAdapter.VueloViewHolder>() {

        class VueloViewHolder(view: View) :
            RecyclerView.ViewHolder(view) {

            val txtAerolinea: TextView =
                view.findViewById(R.id.txtAerolinea)

            val txtRuta: TextView =
                view.findViewById(R.id.txtRuta)

            val txtHorario: TextView =
                view.findViewById(R.id.txtHorario)

            val txtPrecio: TextView =
                view.findViewById(R.id.txtPrecio)

            val btnSeleccionarVuelo: Button =
                view.findViewById(R.id.btnSeleccionarVuelo)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): VueloViewHolder {
            val vista = LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_vuelo,
                    parent,
                    false
                )

            return VueloViewHolder(vista)
        }

        override fun onBindViewHolder(
            holder: VueloViewHolder,
            position: Int
        ) {
            val vuelo = vuelos[position]

            holder.txtAerolinea.text =
                "${vuelo.aerolinea} · ${vuelo.numeroVuelo}"

            holder.txtRuta.text =
                "${vuelo.origen} → ${vuelo.destino}"

            holder.txtHorario.text =
                "${vuelo.horaSalida} – ${vuelo.horaLlegada}"

            val precioTotal =
                vuelo.precio * cantidadPersonas

            holder.txtPrecio.text =
                "Total: ${formatoMoneda(precioTotal)}"

            holder.btnSeleccionarVuelo.setOnClickListener {
                onSeleccionar(vuelo)
            }
        }

        override fun getItemCount(): Int = vuelos.size

        private fun formatoMoneda(precio: Double): String {
            return NumberFormat.getCurrencyInstance(
                Locale("es", "PE")
            ).format(precio)
        }
    }
}

