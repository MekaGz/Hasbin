package com.ramos.myapplication.entidad_usuario

import com.google.gson.annotations.SerializedName

data class nombretel(
@SerializedName("Nombres") var usu_nombres:String="",
@SerializedName("Telefono")var usu_telefono:String=""
)