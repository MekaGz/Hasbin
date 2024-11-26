package com.ramos.myapplication

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ramos.myapplication.databinding.FragmentHasbinearOpBinding
import com.ramos.myapplication.entidad_cuenta.cuenta
import com.ramos.myapplication.entidad_cuenta.cuentaDestinatario
import com.ramos.myapplication.entidad_movimiento.nuevoMovimiento
import com.ramos.myapplication.entidad_movimiento.saldoRequest
import com.ramos.myapplication.servicio.RetroFitCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class hasbinear_op : AppCompatActivity() {

    private lateinit var binding:FragmentHasbinearOpBinding
    private lateinit var telefono_c:String
    private lateinit var destinatario: cuentaDestinatario
    private lateinit var cuenta_uuid_envia:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHasbinearOpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        destinatario = cuentaDestinatario()

        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        cuenta_uuid_envia = sharedPreferences.getString("uuid", null).toString()

        val nombreContacto = intent.getStringExtra("contacto")
        val txtNombre = findViewById<TextView>(R.id.txtNomHasbinear)
        txtNombre.text = nombreContacto

        val telefonoOriginal = intent.getStringExtra("telefono_contacto").toString()

        telefono_c = telefonoOriginal.replace(Regex("[^\\d]"), "")
        Log.d("prueba numero", telefono_c)


        binding.btnHasbinear1.setOnClickListener{
            realizarOperacion(txtNombre)
        }

        binding.btnVolver.setOnClickListener {
            finish()
        }

        binding.btnX1.setOnClickListener{
            finish()
        }

    }

    private fun realizarOperacion(txtNombre: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta = RetroFitCliente.webService.buscarDatosDestinatario(telefono_c)
            runOnUiThread {
                if (respuesta.isSuccessful) {
                    val lista_destinatario = respuesta.body()
                    if (lista_destinatario != null && lista_destinatario.isNotEmpty()) {
                        val destinatario_datos = lista_destinatario[0]
                        destinatario.usu_nombres = destinatario_datos.usu_nombres
                        destinatario.usu_apellidos = destinatario_datos.usu_apellidos
                        destinatario.usu_correo = destinatario_datos.usu_correo
                        destinatario.cuenta_uuid = destinatario_datos.cuenta_uuid
                        destinatario.cuenta_saldo = destinatario_datos.cuenta_saldo

                        agregarMovimiento(cuenta_uuid_envia, destinatario.cuenta_uuid, txtNombre)

                    }
                }
            }

        }
    }

    private fun agregarMovimiento(cuenta_envia:String, cuenta_recibe:String, txtNombre:TextView){
        val monto = binding.editTxtMonto.text.toString().toDouble()
        val mensaje = binding.txtMensajeHasbinear.text.toString()
        val movimientoNuevo = nuevoMovimiento(monto, mensaje, cuenta_recibe, cuenta_envia)
        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetroFitCliente.webService.realizarMovimiento(movimientoNuevo)
            runOnUiThread {
                if(rpta.isSuccessful){
                    enviarContactoNombre(txtNombre)
                    mostrarMensaje("Transferencia por S/${monto} realizada con exito.", monto)
                }
            }
        }
    }

    private fun mostrarMensaje(mensaje:String, monto:Double){
        val ventana = AlertDialog.Builder(this)

        ventana.setTitle("Listo!")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(this, hasbin_exitoso::class.java)
            val destinatario_nomape="${destinatario.usu_nombres}  ${destinatario.usu_apellidos}"
            val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val saldo_cuenta_envia = sharedPreferences.getString("saldo", null).toString().toDouble()
            val cuenta_uuid_envia = sharedPreferences.getString("uuid", null).toString()
            intent.putExtra("contacto_hasbineado", destinatario_nomape)
            intent.putExtra("monto_hasbineado", monto.toString())
            intent.putExtra("correo_destinatario", destinatario.usu_correo)
            actualizarSaldo("sumar", destinatario.cuenta_uuid, destinatario.cuenta_saldo, monto)
            actualizarSaldo("restar", cuenta_uuid_envia, saldo_cuenta_envia, monto)
            startActivity(intent)
        })

        ventana.create().show()

    }

    private fun enviarContactoNombre(nombre:TextView){
        intent.putExtra("contacto_hasbineado", nombre.text)
    }

    private fun actualizarSaldo(tipo:String, uuid:String, saldo:Double, monto:Double){
        if(tipo == "sumar"){
            CoroutineScope(Dispatchers.IO).launch {
                val nuevoSaldo = saldo+monto
                val respuesta = RetroFitCliente.webService.actualizarSaldo(uuid, saldoRequest(nuevoSaldo))
                runOnUiThread {
                    if (respuesta.isSuccessful) {
                        Log.d("Saldo actualizado","Saldo del destinatario actualizado correctamente.")
                    } else {
                        Log.d("Error","Error al actualizar el saldo.")
                    }
                }
            }
        }else{
            CoroutineScope(Dispatchers.IO).launch {
                val nuevoSaldo = saldo-monto
                val respuesta = RetroFitCliente.webService.actualizarSaldo(uuid, saldoRequest(nuevoSaldo))
                runOnUiThread {
                    if (respuesta.isSuccessful) {
                        Log.d("Saldo actualizado","Saldo del remitente actualizado correctamente.")
                    } else {
                        Log.d("Error","Error al actualizar el saldo.")
                    }
                }
            }
        }

    }

}