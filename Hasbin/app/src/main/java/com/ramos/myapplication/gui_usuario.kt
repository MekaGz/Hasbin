package com.ramos.myapplication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.ramos.myapplication.databinding.FragmentGuiUsuarioBinding
import com.ramos.myapplication.databinding.FragmentHasbinearOpBinding
import com.ramos.myapplication.entidad_cuenta.cuenta
import com.ramos.myapplication.entidad_cuenta.cuentaDni
import com.ramos.myapplication.entidad_movimiento.movimiento
import com.ramos.myapplication.entidad_usuario.nombretel
import com.ramos.myapplication.entidad_usuario.usuario
import com.ramos.myapplication.servicio.AppConstantes
import com.ramos.myapplication.servicio.RetroFitCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.math.log

class gui_usuario : AppCompatActivity() {
    private lateinit var binding: FragmentGuiUsuarioBinding
    private lateinit var mDialog: Dialog
    private lateinit var usu: usuario

    private lateinit var usuRe: usuario
    private lateinit var cue: cuenta

    private lateinit var cueRe: cuenta
    private lateinit var mov: movimiento
    var arrayList:ArrayList<ModeloContacto> = arrayListOf()
    var rcAdapter:RCAdapter = RCAdapter(this, arrayList)
    private var listaMovimientos = ArrayList<movimiento>()
    private var adaptador_movimientos:RCMovAdapter = RCMovAdapter()

    companion object {
        private const val REQUEST_CODE_QR = 1001
    }
    private fun initScanner(){
        val intent = Intent(this, CaptureActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_QR)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_QR && resultCode == RESULT_OK) {
            val qrValue = data?.getStringExtra("SCAN_RESULT") // Asegúrate de que esta clave coincida con la que usas para obtener el resultado
            if (qrValue != null) {
                handleQRCode(qrValue)
            }
        }
    }
    private fun handleQRCode(qrValue: String) {

        if (isValidUUID(qrValue))
        {
            CoroutineScope(Dispatchers.IO).launch {

                val rpta = RetroFitCliente.webService.buscarContacto(qrValue)
                Log.d("cuenta qr", "valor uuid: ${qrValue}")
                runOnUiThread {
                    if (rpta.isSuccessful){
                        val lista_tel = rpta.body()

                        Log.d("respuesta", "valor cuenta: ${rpta.body()}")

                        if (lista_tel != null && lista_tel.isNotEmpty()) {
                            val usuario = lista_tel[0]
                            usuRe.usu_nombres=usuario.usu_nombres
                            usuRe.usu_telefono=usuario.usu_telefono
                            Log.d("CONTACTO", "CONTACTO: ${rpta.body()}")

                            val intent = Intent(this@gui_usuario,hasbinear_op::class.java)
                            intent.putExtra("contacto",usuRe.usu_nombres)
                            intent.putExtra("telefono_contacto", usuRe.usu_telefono)
                            this@gui_usuario.startActivity(intent)
                        }
                    }else {
                        Toast.makeText(this@gui_usuario, "Error en la respuesta del servidor.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        } else {
            // Si no es un UUID válido
            showErrorAndScanAgain("El valor escaneado no es un UUID válido.")
        }

    }
    private fun isValidUUID(uuid: String): Boolean {
        return try {
            UUID.fromString(uuid)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
    private fun showErrorAndScanAgain(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        // Iniciar de nuevo el escaneo de QR
        val intent = Intent(this, CaptureActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE_QR)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentGuiUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuRe = usuario()
        cueRe = cuenta()
        usu = usuario()
        cue = cuenta()


        mDialog = Dialog(this)


        binding.rvMovimientos.layoutManager = LinearLayoutManager(this)
        adaptador_movimientos.setContext(this)
        binding.rvMovimientos.adapter = adaptador_movimientos
        actualizarDatosUsuario()


       binding.btnCampana.setOnClickListener {
           val intent = Intent(this, NotificacionesMovimientos::class.java)
           intent.putExtra("usuario_send", usu)
           startActivity(intent)
       }


        binding.btnQR.setOnClickListener{
            initScanner()
        }

        binding.btnHasbinear.setOnClickListener{
            val intent = Intent(this, hasbinearContactos::class.java)
            startActivity(intent)
        }
        binding.btnPerfil.setOnClickListener{
            val intent = Intent(this,perfilqr::class.java)
            startActivity(intent)
        }

        binding.btnActualizar.setOnClickListener {
            actualizarDatosUsuario()
        }
    }

    private fun saveUserCuenta(cuenta: cuenta) {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("uuid", cuenta.cuenta_uuid)
            putString("dni", cuenta.cuenta_usu_dni)
            putString("saldo", cuenta.cuenta_saldo.toString())
            apply()
        }
    }
    private fun actualizarDatosUsuario(){
        usu = intent.getSerializableExtra("usuario_send") as usuario
        actualizarDatosCuenta()
    }

    private fun actualizarDatosCuenta() {
        CoroutineScope(Dispatchers.IO).launch {
            val respuesta = RetroFitCliente.webService.buscarUsuarioCuenta(usu.usu_correo)
            runOnUiThread {
                if (respuesta.isSuccessful) {
                    val lista_cuentas = respuesta.body()
                    if (lista_cuentas != null && lista_cuentas.isNotEmpty()) {
                        val cuenta = lista_cuentas[0]
                        cue.cuenta_uuid = cuenta.cuenta_uuid
                        cue.cuenta_saldo = cuenta.cuenta_saldo
                        cue.cuenta_usu_dni = cuenta.cuenta_usu_dni

                        saveUserCuenta(cuenta)
                        val txtSaldo = findViewById<TextView>(R.id.txtSaldo)



                        txtSaldo.text = "S/ ${cue.cuenta_saldo.toString()}"
                        actualizarDatosMovimientos()
                    }
                }
            }

        }
    }

    /*private fun asignarMovimientos(){
        /*
        binding.rvMovimientos.layoutManager = LinearLayoutManager(this)
        adaptador_movimientos.setContext(this)
        binding.rvMovimientos.adapter = adaptador_movimientos*/
        binding.rvMovimientos.layoutManager = LinearLayoutManager(this)
        adaptador_movimientos.setContext(this)
        binding.rvMovimientos.adapter = adaptador_movimientos

    }*/

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
                        adaptador_movimientos.agregarMovimientos(listaMovimientos)
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