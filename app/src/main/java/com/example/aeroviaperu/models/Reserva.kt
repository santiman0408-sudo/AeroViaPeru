
package com.example.aeroviaperu.models

data class Reserva(
    val id: Long = 0L,
    val codigoReserva: String,
    val clienteNombre: String,
    val telefono: String,
    val numeroVuelo: String,
    val aerolinea: String,
    val origen: String,
    val destino: String,
    val fechaViaje: String,
    val horaSalida: String,
    val horaLlegada: String,
    val cantidadPersonas: Int,
    val precioUnitario: Double,
    val precioTotal: Double,
    val estadoReserva: String = "PENDIENTE",
    val puntoEncuentro: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0
)

