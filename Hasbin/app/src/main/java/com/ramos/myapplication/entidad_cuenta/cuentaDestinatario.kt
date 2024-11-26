package com.ramos.myapplication.entidad_cuenta

import com.google.gson.annotations.SerializedName

data class cuentaDestinatario(
    @SerializedName("Nombres") var usu_nombres:String = "",
    @SerializedName("Apellidos") var usu_apellidos:String = "",
    @SerializedName("Correo") var usu_correo:String = "",
    @SerializedName("UUID") var cuenta_uuid:String = "",
    @SerializedName("Saldo") var cuenta_saldo:Double = 0.0,
)
