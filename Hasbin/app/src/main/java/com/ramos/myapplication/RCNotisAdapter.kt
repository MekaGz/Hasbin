package com.ramos.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ramos.myapplication.entidad_movimiento.movimiento

class RCNotisAdapter: RecyclerView.Adapter<RCNotisAdapter.MiViewHolder>() {

    private var listaMovimientosNotis = ArrayList<movimiento>()
    private lateinit var context: Context

    fun setContextNotis(context: Context){
        this.context = context
    }

    fun agregarMovimientosNotis(movimientos:List<movimiento>){
        listaMovimientosNotis.addAll(movimientos)
        notifyDataSetChanged()
    }

    class MiViewHolder(var view: View) : RecyclerView.ViewHolder(view){

        private var filaMensaje = view.findViewById<TextView>(R.id.txtMensajeCampana)

        fun rellenarFila(movimiento: movimiento) {
            filaMensaje.text = "S/${movimiento.mov_monto} para '${movimiento.mov_nombre_recibe}' '${movimiento.mov_mensaje}'"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RCNotisAdapter.MiViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.campana_mensaje, parent, false)
    )

    override fun getItemCount(): Int {
        return  listaMovimientosNotis.size
    }

    override fun onBindViewHolder(holder: RCNotisAdapter.MiViewHolder, position: Int) {
        val movimientoNotisItem = listaMovimientosNotis[position]
        holder.rellenarFila(movimientoNotisItem)
    }
}