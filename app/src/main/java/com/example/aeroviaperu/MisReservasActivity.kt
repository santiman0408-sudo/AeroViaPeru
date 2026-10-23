package com.example.aeroviaperu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aeroviaperu.data.ReservaDbHelper
import com.example.aeroviaperu.models.Reserva
import java.text.NumberFormat
import java.util.Locale

class MisReservasActivity : AppCompatActivity() {

    private lateinit var dbHelper: ReservaDbHelper
    private lateinit var recycler: RecyclerView
    private lateinit var txtCantidad: TextView
    private lateinit var txtSinReservas: TextView
    private lateinit var btnVolver: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_reservas)

        recycler = findViewById(R.id.recyclerMisReservas)
        txtCantidad = findViewById(R.id.txtCantidadReservas)
        txtSinReservas = findViewById(R.id.txtSinReservas)
        btnVolver = findViewById(R.id.btnVolverVuelos)

        dbHelper = ReservaDbHelper(this)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(false)

        btnVolver.setOnClickListener {
            finish()
        }

        cargarReservas()
    }

    override fun onResume() {
        super.onResume()

        if (::dbHelper.isInitialized && ::recycler.isInitialized) {
            cargarReservas()
        }
    }

    private fun cargarReservas() {
        val reservas = dbHelper.obtenerReservas()

        txtCantidad.text = when (reservas.size) {
            0 -> "No tienes viajes registrados"
            1 -> "Tienes 1 reserva registrada"
            else -> "Tienes ${reservas.size} reservas registradas"
        }

        val hayReservas = reservas.isNotEmpty()

        txtSinReservas.visibility =
            if (hayReservas) View.GONE else View.VISIBLE

        recycler.visibility =
            if (hayReservas) View.VISIBLE else View.GONE

        recycler.adapter = ReservaAdapter(reservas)
    }

    private inner class ReservaAdapter(
        private val reservas: List<Reserva>
    ) : RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {

        inner class ReservaViewHolder(view: View) :
            RecyclerView.ViewHolder(view) {

            val codigo: TextView =
                view.findViewById(R.id.txtCodigoReserva)

            val cliente: TextView =
                view.findViewById(R.id.txtClienteReserva)

            val ruta: TextView =
                view.findViewById(R.id.txtRutaReserva)

            val fecha: TextView =
                view.findViewById(R.id.txtFechaReserva)

            val pasajeros: TextView =
                view.findViewById(R.id.txtPasajerosReserva)

            val precio: TextView =
                view.findViewById(R.id.txtPrecioReserva)

            val estado: TextView =
                view.findViewById(R.id.txtEstadoReserva)

            val btnEditar: Button =
                view.findViewById(R.id.btnEditarReservaItem)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ReservaViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_reserva, parent, false)

            return ReservaViewHolder(view)
        }

        override fun onBindViewHolder(
            holder: ReservaViewHolder,
            position: Int
        ) {
            val reserva = reservas[position]

            holder.codigo.text =
                "Reserva ${reserva.codigoReserva}"

            holder.cliente.text =
                "Pasajero: ${reserva.clienteNombre}"

            holder.ruta.text =
                "${reserva.origen} → ${reserva.destino}"

            holder.fecha.text =
                "Fecha: ${reserva.fechaViaje} · " +
                        "Vuelo ${reserva.numeroVuelo} · " +
                        "${reserva.horaSalida}–${reserva.horaLlegada}"

            holder.pasajeros.text =
                "Pasajeros: ${reserva.cantidadPersonas}"

            holder.precio.text =
                "Total: ${NumberFormat.getCurrencyInstance(
                    Locale("es", "PE")
                ).format(reserva.precioTotal)}"

            holder.estado.text = reserva.estadoReserva

            holder.btnEditar.setOnClickListener {
                val intent = Intent(
                    this@MisReservasActivity,
                    EditarReservaActivity::class.java
                )

                intent.putExtra("reserva_id", reserva.id)

                startActivityForResult(intent, REQUEST_EDITAR_RESERVA)
            }
        }

        override fun getItemCount(): Int = reservas.size
    }

    @Deprecated("Usa el resultado de la actividad de edición para actualizar la lista")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_EDITAR_RESERVA &&
            resultCode == RESULT_OK
        ) {
            cargarReservas()

            Toast.makeText(
                this,
                "Lista de reservas actualizada",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        if (::dbHelper.isInitialized) {
            dbHelper.close()
        }

        super.onDestroy()
    }

    companion object {
        private const val REQUEST_EDITAR_RESERVA = 1001
    }
}
