package com.ramos.myapplication

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.ramos.myapplication.databinding.FragmentGuiUsuarioBinding
import com.ramos.myapplication.databinding.FragmentPerfilqrBinding
import com.ramos.myapplication.entidad_cuenta.cuenta
import com.ramos.myapplication.entidad_usuario.usuario
import com.ramos.myapplication.servicio.RetroFitCliente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class perfilqr : AppCompatActivity() {
    private lateinit var binding: FragmentPerfilqrBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var ivQRcode: ImageView
    private var nombres: String? = null
    private var apellidos: String? = null
    private var telefono: String? = null
    private var uuid:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPerfilqrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ivQRcode=findViewById(R.id.CodigoQr)

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        uuid = sharedPreferences.getString("uuid",null)
        nombres = sharedPreferences.getString("nombres", null)
        apellidos = sharedPreferences.getString("apellidos", null)
        telefono = sharedPreferences.getString("telefono", null)

        val txtPerfil = findViewById<TextView>(R.id.txtNombrePerfil)
        txtPerfil.text = "${nombres} ${apellidos} - ${telefono}"

        generar()

        binding.btnHasbinear1.setOnClickListener{
            finish()
        }
    }

    private fun generar(){
        val data = uuid
        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(data,BarcodeFormat.QR_CODE,512,512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565)
            for(x in 0 until width){
                for (y in 0 until height){
                    bmp.setPixel(x,y,if (bitMatrix[x,y])Color.BLACK else Color.WHITE)
                }
            }
            ivQRcode.setImageBitmap(bmp)
        }catch (e: WriterException){}
    }

}