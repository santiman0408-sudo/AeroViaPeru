package com.example.aeroviaperu.models

data class Vuelo(
    val numeroVuelo: String,
    val aerolinea: String,
    val origen: String,
    val destino: String,
    val horaSalida: String,
    val horaLlegada: String,
    val precio: Double
)
