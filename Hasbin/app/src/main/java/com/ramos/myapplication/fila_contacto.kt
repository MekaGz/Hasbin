package com.ramos.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ramos.myapplication.databinding.FilaContactoBinding

class fila_contacto : AppCompatActivity(){
    private lateinit var binding:FilaContactoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FilaContactoBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.txtNombreContacto.setOnClickListener {
            val intent = Intent(this, hasbinear_op::class.java)
            startActivity(intent)
        }
    }
}