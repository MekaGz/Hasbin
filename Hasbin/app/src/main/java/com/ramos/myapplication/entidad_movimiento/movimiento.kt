package com.ramos.myapplication.entidad_movimiento

import com.google.gson.annotations.SerializedName

data class movimiento(
    @SerializedName("ID") var mov_id: Int,
    @SerializedName("Monto") var mov_monto: Double,
    @SerializedName("Fecha") var mov_fecha: String?,
    @SerializedName("DiaMes") var mov_diames: String?,
    @SerializedName("Hora") var mov_hora: String?,
    @SerializedName("Mensaje") var mov_mensaje: String?,
    @SerializedName("NombreRecibe") var mov_nombre_recibe: String?,
    @SerializedName("cuenta_uuid_envia") var mov_cuenta_envia: String?,
    @SerializedName("cuenta_uuid_recibe") var mov_cuenta_recibe: String?
)
