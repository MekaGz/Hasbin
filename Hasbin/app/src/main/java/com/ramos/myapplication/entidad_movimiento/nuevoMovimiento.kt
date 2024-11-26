package com.ramos.myapplication.entidad_movimiento

data class nuevoMovimiento(
    val mov_monto: Double,
    val mov_mensaje: String,
    val cuenta_uuid_recibe: String,
    val cuenta_uuid_envia: String
)
