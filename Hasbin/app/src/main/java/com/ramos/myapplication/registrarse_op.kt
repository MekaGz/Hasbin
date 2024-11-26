package com.ramos.myapplication

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.ramos.myapplication.databinding.FragmentRegistrarseOpBinding
import com.ramos.myapplication.entidad_cuenta.cuenta
import com.ramos.myapplication.entidad_usuario.usuario
import com.ramos.myapplication.servicio.RetroFitCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID


class registrarse_op : AppCompatActivity() {

    private lateinit var binding: FragmentRegistrarseOpBinding
    private lateinit var txtFechaNacimiento: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = FragmentRegistrarseOpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegistrarseR.setOnClickListener{
            registrarUsuarios()
        }

        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this, login_op::class.java)
            startActivity(intent)
        }
        txtFechaNacimiento = findViewById(R.id.txtFechaNacimiento)

        txtFechaNacimiento.setOnClickListener {
            showDatePickerDialog()
        }
    }
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // Formatear la fecha seleccionada
            val formattedDate = "$selectedYear-${formatTwoDigits(selectedMonth + 1)}-${formatTwoDigits(selectedDay)}"
            txtFechaNacimiento.setText(formattedDate) // Establecer la fecha en el campo de texto
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun formatTwoDigits(number: Int): String {
        return if (number < 10) "0$number" else number.toString()
    }
    private fun uuid():String{
        return UUID.randomUUID().toString()
    }

    private fun registrarUsuarios(){
        val dni = binding.txtDniRe.text.toString()
        val nombres = binding.txtNombresRe.text.toString()
        val apellidos = binding.txtApellidosRe.text.toString()
        val telefono = binding.txtTelefonoRe.text.toString()
        val correo = binding.txtCorreoRe.text.toString()
        val clave = binding.txtClaveRe.text.toString()
        val fecha_nac = binding.txtFechaNacimiento.text.toString()

        if (!dni.matches(Regex("\\d{8}"))) {
            mostrarMensaje("El DNI debe contener exactamente 8 dígitos")
            return
        }
        if (!telefono.matches(Regex("\\d{9}"))) {
            mostrarMensaje("El teléfono debe contener exactamente 9 dígitos")
            return
        }
        val usuario = usuario(dni,nombres,apellidos,telefono,correo,clave,fecha_nac)

        CoroutineScope(Dispatchers.IO).launch {
            val rpta = RetroFitCliente.webService.agregarUsuario(usuario)
            runOnUiThread {
                if(rpta.isSuccessful){
                    mostrarMensaje(rpta.body().toString())

                    val cu_uuid=uuid();
                    val cuenta =cuenta(cu_uuid,0.0,usuario.usu_dni)
                    CoroutineScope(Dispatchers.IO).launch {
                        val rpta = RetroFitCliente.webService.agregarCuenta(cuenta)
                        runOnUiThread {
                            if(rpta.isSuccessful){
                                mostrarMensaje(rpta.body().toString())
                            }
                        }
                    }
                }
            }
        }


    }

    private fun mostrarMensaje(mensaje:String){
        val ventana = AlertDialog.Builder(this)

        ventana.setTitle("Bienvenido!")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(this,login_op::class.java)
            startActivity(intent)
        })

        ventana.create().show()

    }

}