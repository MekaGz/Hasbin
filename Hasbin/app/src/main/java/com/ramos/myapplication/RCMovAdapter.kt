package com.ramos.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ramos.myapplication.entidad_movimiento.movimiento

class RCMovAdapter: RecyclerView.Adapter<RCMovAdapter.MiViewHolder>() {

    private var listaMovimientos = ArrayList<movimiento>()
    private lateinit var context: Context

    fun setContext(context: Context){
        this.context = context
    }

    fun agregarMovimientos(movimientos:List<movimiento>){
        listaMovimientos.addAll(movimientos)
        notifyDataSetChanged()
    }

    class MiViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var filaNombres = view.findViewById<TextView>(R.id.txtPerNombresGUI)
        private var filaFecha = view.findViewById<TextView>(R.id.txtHoraPerGUI)
        private var filaMonto = view.findViewById<TextView>(R.id.txtMontoPerGUI)

        fun rellenarFila(movimiento: movimiento) {
            filaNombres.text = "Para: ${movimiento.mov_nombre_recibe}"
            var fecha_movimiento = "${movimiento.mov_diames} ${movimiento.mov_hora}"
            filaFecha.text = fecha_movimiento
            filaMonto.text = movimiento.mov_monto.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MiViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.fila_movimientos,parent,false)
    )

    override fun getItemCount(): Int {
        return listaMovimientos.size
    }

    override fun onBindViewHolder(holder: RCMovAdapter.MiViewHolder, position: Int) {
        val movimientoItem = listaMovimientos[position]
        holder.rellenarFila(movimientoItem)
    }

}