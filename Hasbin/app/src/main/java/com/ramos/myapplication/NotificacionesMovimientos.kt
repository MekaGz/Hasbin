package com.ramos.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.ramos.myapplication.databinding.ActivityNotificacionesMovimientosBinding
import com.ramos.myapplication.entidad_cuenta.cuenta
import com.ramos.myapplication.entidad_usuario.usuario
import com.ramos.myapplication.servicio.AppConstantes
import com.ramos.myapplication.servicio.RetroFitCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificacionesMovimientos : AppCompatActivity() {
    private lateinit var binding: ActivityNotificacionesMovimientosBinding
    private lateinit var usu: usuario
    private lateinit var cue: cuenta
    private var adaptador_notis: RCNotisAdapter = RCNotisAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificacionesMovimientosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window = window
        val layoutParams = window.attributes
        layoutParams.width = (resources.displayMetrics.widthPixels * 0.8).toInt()  // Ajustar al 80% del ancho de la pantalla
        layoutParams.height = (resources.displayMetrics.heightPixels * 0.8).toInt() // Ajustar al 50% del alto de la pantalla
        window.attributes = layoutParams

        // Opcional: para un efecto de aparición suave
        window.setWindowAnimations(android.R.style.Animation_Dialog)

        usu = usuario()
        cue = cuenta()

        binding.rcNotis.layoutManager = LinearLayoutManager(this)
        adaptador_notis.setContextNotis(this)
        binding.rcNotis.adapter = adaptador_notis
        actualizarDatosUsuario()

        binding.btnXNotis.setOnClickListener {
            finish()
        }
    }

    private fun actualizarDatosUsuario(){
        usu = intent.getSerializableExtra("usuario_send") as usuario
        if (usu == null) {
            Log.e("NotificacionesMovimientos", "El usuario recibido es nulo")
            // Manejo del caso donde el usuario no fue pasado correctamente
        }else{
            actualizarDatosCuenta()
        }
    }

    private fun actualizarDatosCuenta(){
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        cue.cuenta_uuid = sharedPreferences.getString("uuid", null).toString()
        cue.cuenta_saldo = sharedPreferences.getString("saldo", null).toString().toDouble()
        cue.cuenta_usu_dni = sharedPreferences.getString("dni", null).toString()
        actualizarDatosMovimientos()
    }
    private fun actualizarDatosMovimientos(){

        //asignarMovimientos()

        var cuentaid = cue.cuenta_uuid

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("cuenta_uuid", "Valor de cuenta_uuid: ${cuentaid}")
            Log.d("API URL", "URL a llamar: ${AppConstantes.BASE_URL}cuenta/movimientos/${cuentaid}")
            val respuesta = RetroFitCliente.webService.buscarUsuarioMovimientos(cuentaid)
            runOnUiThread {
                if (respuesta.isSuccessful) {
                    /*
                    val lista_movimientos = respuesta.body()

                    Log.d("Movimientos", "Datos obtenidos: $lista_movimientos")
                    if (lista_movimientos != null && lista_movimientos.isNotEmpty()) {
                        listaMovimientos.clear()

                        listaMovimientos.addAll(lista_movimientos)
                        adaptador_movimientos.agregarMovimientos(listaMovimientos)
                        adaptador_movimientos.notifyDataSetChanged()*/
                    /*
                    listaMovimientos= respuesta.body()!!.listaMovimiento
                    Log.d("===", "Cantidad: " + listaMovimientos.size)
                    adaptador_movimientos.agregarMovimientos(listaMovimientos)
                    adaptador_movimientos.notifyDataSetChanged()*/
                    val movimientosResponse = respuesta.body()
                    if (movimientosResponse != null && movimientosResponse.listaMovimiento.isNotEmpty()) {
                        val listaMovimientos = movimientosResponse.listaMovimiento // Ignora el segundo elemento
                        adaptador_notis.agregarMovimientosNotis(listaMovimientos)
                        Log.d("Movimientos", "Datos obtenidos: $listaMovimientos")
                    }


                }else{
                    Log.d("Error api", "Error al obtener movimientos")
                    Log.d("Movimientos", "Error en la respuesta: ${respuesta.errorBody()?.string()}")
                    Log.d("Error api", "cuenta-uuid = ${cue.cuenta_uuid}")

                }
            }

        }
    }
}