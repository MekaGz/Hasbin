package com.ramos.myapplication.entidad_cuenta

data class cuenta(
    var cuenta_uuid:String = "",
    var cuenta_saldo:Double = 0.0,
    var cuenta_usu_dni:String = "",
)
