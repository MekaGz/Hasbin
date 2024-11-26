package com.ramos.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ramos.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //incompleto

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIngresarInicio.setOnClickListener{
            val intent = Intent(this, login_op::class.java)
            startActivity(intent)
        }

        binding.btnRegistrarse1.setOnClickListener{
            val intent = Intent(this, registrarse_op::class.java)
            startActivity(intent)
        }
    }
}