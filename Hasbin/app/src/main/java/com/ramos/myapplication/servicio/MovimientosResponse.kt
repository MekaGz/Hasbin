package com.ramos.myapplication.servicio

import com.google.gson.annotations.SerializedName
import com.ramos.myapplication.entidad_movimiento.movimiento

data class MovimientosResponse(
    @SerializedName("listaMovimientos") var listaMovimiento: List<movimiento> = listOf()
)
