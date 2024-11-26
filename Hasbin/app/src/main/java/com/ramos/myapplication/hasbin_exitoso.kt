package com.ramos.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.ramos.myapplication.databinding.FragmentHasbinExitosoBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class hasbin_exitoso : AppCompatActivity() {

    private lateinit var binding: FragmentHasbinExitosoBinding
    private lateinit var imagePath: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHasbinExitosoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nombreContactoHasbineado = intent.getStringExtra("contacto_hasbineado")
        val txtNombreHasbineado = findViewById<TextView>(R.id.txtNombreHasbineaste)
        txtNombreHasbineado.text = "a "+nombreContactoHasbineado

        val montoHasbineado = intent.getStringExtra("monto_hasbineado")
        val txtSaldoHasbineado = findViewById<TextView>(R.id.txtSaldoHasbineaste)

        txtSaldoHasbineado.text = "S/${montoHasbineado}"

        binding.btnVolverInicio.setOnClickListener{
            val intent = Intent(this,gui_usuario::class.java)
            startActivity(intent)
        }

        binding.btnCompartir.setOnClickListener (View.OnClickListener {
            val bitmap = takeScreenshot()
            saveBitmap(bitmap!!)
            shareIt()
        })
    }

    fun takeScreenshot(): Bitmap? {
        val rootView = findViewById<View>(android.R.id.content).rootView
        rootView.isDrawingCacheEnabled = true
        return rootView.drawingCache
    }

    fun saveBitmap(bitmap: Bitmap) {
        imagePath = File(getExternalFilesDir(null), "screenshot.png")
        val fos: FileOutputStream
        try {
            fos = FileOutputStream(imagePath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.e("GREC", e.message, e)
        } catch (e: IOException) {
            Log.e("GREC", e.message, e)
        }
    }

    private fun shareIt() {
        val uri = FileProvider.getUriForFile(
            this,
            "${applicationContext.packageName}.fileprovider",
            imagePath
        )
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.setType("image/*")
        val shareBody = ""
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Transferencia exitosa!")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }


}