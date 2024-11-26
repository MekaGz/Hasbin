package com.ramos.myapplication

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ramos.myapplication.databinding.FragmentLoginOpBinding
import com.ramos.myapplication.entidad_usuario.usuario
import com.ramos.myapplication.servicio.RetroFitCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class login_op : AppCompatActivity() {

    private lateinit var binding: FragmentLoginOpBinding
    private lateinit var usu: usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginOpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usu = usuario()

        verificar()
    }

    private fun verificar(){
        binding.btnLogearte.setOnClickListener {
            val correo_log = binding.txtCorreoLog.text.toString()
            val clave_log = binding.txtClaveLog.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val respuesta = RetroFitCliente.webService.buscarUsuarioClave(correo_log, clave_log)

                Log.d("respuesta", "valor cuenta: ${respuesta.body()}")
                runOnUiThread {
                    if (respuesta.isSuccessful) {
                        val lista_usuarios = respuesta.body()

                        if (lista_usuarios != null && lista_usuarios.isNotEmpty()) {
                            val usuario = lista_usuarios[0]
                            usu.usu_dni = usuario.usu_dni
                            usu.usu_nombres = usuario.usu_nombres
                            usu.usu_apellidos = usuario.usu_apellidos
                            usu.usu_correo = usuario.usu_correo
                            usu.usu_telefono = usuario.usu_telefono
                            usu.usu_clave = ""
                            usu.usu_fecha_nac = usuario.usu_fecha_nac
                            saveUserUsuario(usuario)
                            mostrarMensaje(usuario.usu_nombres.toString())
                        } else {
                            mostrarMensaje("La lista de usuarios está vacía o es nula.")}
                    }else {
                        Toast.makeText(this@login_op, "Error en la respuesta del servidor.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun saveUserUsuario(usuario: usuario) {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("dni", usuario.usu_dni)
            putString("nombres", usuario.usu_nombres)
            putString("apellidos", usuario.usu_apellidos)
            putString("telefono",usuario.usu_telefono)
            apply()
        }
    }

    private fun mostrarMensaje(mensaje:String){
        val ventana = AlertDialog.Builder(this)

        val usuario_send = "usuario_send"

        ventana.setTitle("Bienvenido!")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(this,gui_usuario::class.java)
            if(usu.usu_dni.isNullOrBlank()){

            }else{
                intent.putExtra(usuario_send, usu)
            }

            startActivity(intent)
        })

        ventana.create().show()
    }

}