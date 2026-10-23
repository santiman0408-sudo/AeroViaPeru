package com.example.aeroviaperu.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.aeroviaperu.models.Reserva

class ReservaDbHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    companion object {
        private const val DATABASE_NAME = "aerovia_peru.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_RESERVAS = "reservas"

        private const val COL_ID = "id"
        private const val COL_CODIGO = "codigo_reserva"
        private const val COL_CLIENTE = "cliente_nombre"
        private const val COL_TELEFONO = "telefono"
        private const val COL_NUMERO_VUELO = "numero_vuelo"
        private const val COL_AEROLINEA = "aerolinea"
        private const val COL_ORIGEN = "origen"
        private const val COL_DESTINO = "destino"
        private const val COL_FECHA = "fecha_viaje"
        private const val COL_HORA_SALIDA = "hora_salida"
        private const val COL_HORA_LLEGADA = "hora_llegada"
        private const val COL_CANTIDAD = "cantidad_personas"
        private const val COL_PRECIO_UNITARIO = "precio_unitario"
        private const val COL_PRECIO_TOTAL = "precio_total"
        private const val COL_ESTADO = "estado_reserva"
        private const val COL_PUNTO = "punto_encuentro"
        private const val COL_LATITUD = "latitud"
        private const val COL_LONGITUD = "longitud"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE $TABLE_RESERVAS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CODIGO TEXT NOT NULL UNIQUE,
                $COL_CLIENTE TEXT NOT NULL,
                $COL_TELEFONO TEXT NOT NULL,
                $COL_NUMERO_VUELO TEXT NOT NULL,
                $COL_AEROLINEA TEXT NOT NULL,
                $COL_ORIGEN TEXT NOT NULL,
                $COL_DESTINO TEXT NOT NULL,
                $COL_FECHA TEXT NOT NULL,
                $COL_HORA_SALIDA TEXT NOT NULL,
                $COL_HORA_LLEGADA TEXT NOT NULL,
                $COL_CANTIDAD INTEGER NOT NULL,
                $COL_PRECIO_UNITARIO REAL NOT NULL,
                $COL_PRECIO_TOTAL REAL NOT NULL,
                $COL_ESTADO TEXT NOT NULL DEFAULT 'PENDIENTE',
                $COL_PUNTO TEXT NOT NULL DEFAULT '',
                $COL_LATITUD REAL NOT NULL DEFAULT 0,
                $COL_LONGITUD REAL NOT NULL DEFAULT 0
            )
        """.trimIndent()

        db.execSQL(sql)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        // Agrega aquí las migraciones necesarias cuando
        // cambie la estructura de la base de datos.
        // No eliminar la tabla para conservar las reservas.
    }

    /**
     * Registra una reserva y devuelve su ID.
     * Devuelve -1 si ocurre un error de inserción.
     */
    fun insertarReserva(reserva: Reserva): Long {
        val values = crearValoresReserva(reserva)

        return writableDatabase.insert(
            TABLE_RESERVAS,
            null,
            values
        )
    }

    /**
     * Devuelve todas las reservas, de la más reciente
     * a la más antigua.
     */
    fun obtenerReservas(): List<Reserva> {
        val reservas = mutableListOf<Reserva>()

        val cursor = readableDatabase.query(
            TABLE_RESERVAS,
            null,
            null,
            null,
            null,
            null,
            "$COL_ID DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                reservas.add(
                    Reserva(
                        id = it.getLong(
                            it.getColumnIndexOrThrow(COL_ID)
                        ),
                        codigoReserva = it.getString(
                            it.getColumnIndexOrThrow(COL_CODIGO)
                        ),
                        clienteNombre = it.getString(
                            it.getColumnIndexOrThrow(COL_CLIENTE)
                        ),
                        telefono = it.getString(
                            it.getColumnIndexOrThrow(COL_TELEFONO)
                        ),
                        numeroVuelo = it.getString(
                            it.getColumnIndexOrThrow(COL_NUMERO_VUELO)
                        ),
                        aerolinea = it.getString(
                            it.getColumnIndexOrThrow(COL_AEROLINEA)
                        ),
                        origen = it.getString(
                            it.getColumnIndexOrThrow(COL_ORIGEN)
                        ),
                        destino = it.getString(
                            it.getColumnIndexOrThrow(COL_DESTINO)
                        ),
                        fechaViaje = it.getString(
                            it.getColumnIndexOrThrow(COL_FECHA)
                        ),
                        horaSalida = it.getString(
                            it.getColumnIndexOrThrow(COL_HORA_SALIDA)
                        ),
                        horaLlegada = it.getString(
                            it.getColumnIndexOrThrow(COL_HORA_LLEGADA)
                        ),
                        cantidadPersonas = it.getInt(
                            it.getColumnIndexOrThrow(COL_CANTIDAD)
                        ),
                        precioUnitario = it.getDouble(
                            it.getColumnIndexOrThrow(COL_PRECIO_UNITARIO)
                        ),
                        precioTotal = it.getDouble(
                            it.getColumnIndexOrThrow(COL_PRECIO_TOTAL)
                        ),
                        estadoReserva = it.getString(
                            it.getColumnIndexOrThrow(COL_ESTADO)
                        ),
                        puntoEncuentro = it.getString(
                            it.getColumnIndexOrThrow(COL_PUNTO)
                        ),
                        latitud = it.getDouble(
                            it.getColumnIndexOrThrow(COL_LATITUD)
                        ),
                        longitud = it.getDouble(
                            it.getColumnIndexOrThrow(COL_LONGITUD)
                        )
                    )
                )
            }
        }

        return reservas
    }

    /**
     * Busca una reserva por su ID.
     * Devuelve null si no existe.
     */
    fun obtenerReservaPorId(id: Long): Reserva? {
        if (id <= 0L) return null

        val cursor = readableDatabase.query(
            TABLE_RESERVAS,
            null,
            "$COL_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            "1"
        )

        cursor.use {
            if (!it.moveToFirst()) {
                return null
            }

            return Reserva(
                id = it.getLong(
                    it.getColumnIndexOrThrow(COL_ID)
                ),
                codigoReserva = it.getString(
                    it.getColumnIndexOrThrow(COL_CODIGO)
                ),
                clienteNombre = it.getString(
                    it.getColumnIndexOrThrow(COL_CLIENTE)
                ),
                telefono = it.getString(
                    it.getColumnIndexOrThrow(COL_TELEFONO)
                ),
                numeroVuelo = it.getString(
                    it.getColumnIndexOrThrow(COL_NUMERO_VUELO)
                ),
                aerolinea = it.getString(
                    it.getColumnIndexOrThrow(COL_AEROLINEA)
                ),
                origen = it.getString(
                    it.getColumnIndexOrThrow(COL_ORIGEN)
                ),
                destino = it.getString(
                    it.getColumnIndexOrThrow(COL_DESTINO)
                ),
                fechaViaje = it.getString(
                    it.getColumnIndexOrThrow(COL_FECHA)
                ),
                horaSalida = it.getString(
                    it.getColumnIndexOrThrow(COL_HORA_SALIDA)
                ),
                horaLlegada = it.getString(
                    it.getColumnIndexOrThrow(COL_HORA_LLEGADA)
                ),
                cantidadPersonas = it.getInt(
                    it.getColumnIndexOrThrow(COL_CANTIDAD)
                ),
                precioUnitario = it.getDouble(
                    it.getColumnIndexOrThrow(COL_PRECIO_UNITARIO)
                ),
                precioTotal = it.getDouble(
                    it.getColumnIndexOrThrow(COL_PRECIO_TOTAL)
                ),
                estadoReserva = it.getString(
                    it.getColumnIndexOrThrow(COL_ESTADO)
                ),
                puntoEncuentro = it.getString(
                    it.getColumnIndexOrThrow(COL_PUNTO)
                ),
                latitud = it.getDouble(
                    it.getColumnIndexOrThrow(COL_LATITUD)
                ),
                longitud = it.getDouble(
                    it.getColumnIndexOrThrow(COL_LONGITUD)
                )
            )
        }
    }

    /**
     * Actualiza los campos editables de una reserva existente.
     * Devuelve el número de filas actualizadas.
     */
    fun actualizarReserva(reserva: Reserva): Int {
        require(reserva.id > 0L) {
            "El ID de la reserva no es válido"
        }

        require(reserva.clienteNombre.isNotBlank()) {
            "El nombre del pasajero es obligatorio"
        }

        require(reserva.telefono.isNotBlank()) {
            "El teléfono es obligatorio"
        }

        require(reserva.cantidadPersonas in 1..9) {
            "La cantidad de pasajeros debe estar entre 1 y 9"
        }

        require(reserva.precioUnitario >= 0.0) {
            "La tarifa unitaria no puede ser negativa"
        }

        require(reserva.precioTotal >= 0.0) {
            "El precio total no puede ser negativo"
        }

        val values = ContentValues().apply {
            put(COL_CLIENTE, reserva.clienteNombre)
            put(COL_TELEFONO, reserva.telefono)
            put(COL_FECHA, reserva.fechaViaje)
            put(COL_CANTIDAD, reserva.cantidadPersonas)
            put(COL_PRECIO_TOTAL, reserva.precioTotal)
        }

        return writableDatabase.update(
            TABLE_RESERVAS,
            values,
            "$COL_ID = ?",
            arrayOf(reserva.id.toString())
        )
    }

    /**
     * Actualiza el estado de una reserva.
     */
    fun actualizarEstado(
        id: Long,
        nuevoEstado: String
    ): Int {
        val estadosPermitidos = setOf(
            "PENDIENTE",
            "CONFIRMADO",
            "FINALIZADO",
            "CANCELADO"
        )

        require(nuevoEstado in estadosPermitidos) {
            "El estado de la reserva no es válido"
        }

        val values = ContentValues().apply {
            put(COL_ESTADO, nuevoEstado)
        }

        return writableDatabase.update(
            TABLE_RESERVAS,
            values,
            "$COL_ID = ?",
            arrayOf(id.toString())
        )
    }

    /**
     * Elimina una reserva por ID.
     */
    fun eliminarReserva(id: Long): Int {
        require(id > 0L) {
            "El ID de la reserva no es válido"
        }

        return writableDatabase.delete(
            TABLE_RESERVAS,
            "$COL_ID = ?",
            arrayOf(id.toString())
        )
    }

    /**
     * Construye los valores de una reserva para insertarla.
     */
    private fun crearValoresReserva(
        reserva: Reserva
    ): ContentValues {
        return ContentValues().apply {
            put(COL_CODIGO, reserva.codigoReserva)
            put(COL_CLIENTE, reserva.clienteNombre)
            put(COL_TELEFONO, reserva.telefono)
            put(COL_NUMERO_VUELO, reserva.numeroVuelo)
            put(COL_AEROLINEA, reserva.aerolinea)
            put(COL_ORIGEN, reserva.origen)
            put(COL_DESTINO, reserva.destino)
            put(COL_FECHA, reserva.fechaViaje)
            put(COL_HORA_SALIDA, reserva.horaSalida)
            put(COL_HORA_LLEGADA, reserva.horaLlegada)
            put(COL_CANTIDAD, reserva.cantidadPersonas)
            put(COL_PRECIO_UNITARIO, reserva.precioUnitario)
            put(COL_PRECIO_TOTAL, reserva.precioTotal)
            put(COL_ESTADO, reserva.estadoReserva)
            put(COL_PUNTO, reserva.puntoEncuentro)
            put(COL_LATITUD, reserva.latitud)
            put(COL_LONGITUD, reserva.longitud)
        }
    }
}
