package com.ramos.myapplication.servicio

import com.ramos.myapplication.entidad_usuario.usuario
import com.google.gson.GsonBuilder
import com.ramos.myapplication.entidad_cuenta.cuenta
import com.ramos.myapplication.entidad_cuenta.cuentaDestinatario
import com.ramos.myapplication.entidad_cuenta.cuentaDni
import com.ramos.myapplication.entidad_movimiento.movimiento
import com.ramos.myapplication.entidad_movimiento.nuevoMovimiento
import com.ramos.myapplication.entidad_movimiento.saldoRequest
import com.ramos.myapplication.entidad_usuario.nombretel
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.PUT

object AppConstantes{
    const val BASE_URL = "http://192.168.18.35:3000/"
}

interface WebService{


    @GET("/{cuenta}/contacto")
    suspend fun buscarContacto(@Path("cuenta")cuenta: String):Response<List<nombretel>>

    @GET("/usucla/{correo}/{clave}")
    suspend fun buscarUsuarioClave(@Path("correo") correo:String, @Path("clave") clave:String): Response<List<usuario>>

    @GET("/cuentas/usuario/correo/{correo}")
    suspend fun buscarUsuarioCuenta(@Path("correo") correo:String): Response<List<cuenta>>

    @GET("/cuentas/usuario/{dni}")
    suspend fun buscarUsuario(@Path("dni") dni:String): Response<List<usuario>>

    @GET("/cuenta/movimientos/{cuenta}")
    suspend fun buscarUsuarioMovimientos(@Path("cuenta") cuenta:String): Response<MovimientosResponse>

    @GET("/cuentas/destinatario/telefono/{telefono}")
    suspend fun buscarDatosDestinatario(@Path("telefono") telefono:String): Response<List<cuentaDestinatario>>

    @POST("/usuario/agregar")
    suspend fun agregarUsuario(@Body usuario: usuario): Response<String>

    @POST("/cuenta/agregar")
    suspend fun agregarCuenta(@Body cuenta: cuenta): Response<String>

    @POST("/movimiento/agregar")
    suspend fun realizarMovimiento(@Body nuevoMovimiento: nuevoMovimiento): Response<String>

    @PUT("/cuenta/actualizarsaldo/{uuid}")
    suspend fun actualizarSaldo(@Path("uuid") uuid: String, @Body saldoActual: saldoRequest): Response<String>

}

object RetroFitCliente{
    val webService:WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().create()))
            .build()
            .create(WebService::class.java)
    }
}