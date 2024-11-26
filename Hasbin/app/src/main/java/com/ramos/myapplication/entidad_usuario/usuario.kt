package com.ramos.myapplication.entidad_usuario

import java.io.Serializable

data class usuario(
    var usu_dni:String = "",
    var usu_nombres:String = "",
    var usu_apellidos:String = "",
    var usu_telefono:String = "",
    var usu_correo:String = "",
    var usu_clave:String = "",
    var usu_fecha_nac:String = "",

) : Serializable
