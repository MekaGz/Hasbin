package com.ramos.myapplication

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ramos.myapplication.databinding.FilaContactoBinding

class RCAdapter(val context:Context,
    val listaContacto: ArrayList<ModeloContacto>
) :RecyclerView.Adapter<RCAdapter.MyViewHolder>(){

    inner class MyViewHolder(val binding:FilaContactoBinding) : RecyclerView.ViewHolder(binding.root){
        val nombreContacto = binding.txtNombreContacto
        val numeroContacto = binding.txtNumeroContacto
        val layoutItem = binding.layoutItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(FilaContactoBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = listaContacto.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = listaContacto[position]
        holder.binding.txtNombreContacto.text = item.NombreContacto
        holder.binding.txtNumeroContacto.text = item.NumeroContacto
        holder.layoutItem.setOnClickListener {
            val intent = Intent(context, hasbinear_op::class.java)
            intent.putExtra("contacto", item.NombreContacto)
            intent.putExtra("telefono_contacto", item.NumeroContacto)
            context.startActivity(intent)
        }
    }

}