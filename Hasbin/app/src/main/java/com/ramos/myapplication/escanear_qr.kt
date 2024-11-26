package com.ramos.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.ramos.myapplication.databinding.FragmentEscanearQrBinding

class escanear_qr : AppCompatActivity() {

    private lateinit var binding: FragmentEscanearQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentEscanearQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnX.setOnClickListener{
            finish()
        }

    }
}