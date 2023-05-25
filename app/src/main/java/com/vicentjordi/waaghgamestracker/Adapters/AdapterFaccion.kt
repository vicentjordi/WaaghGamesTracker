package com.vicentjordi.waaghgamestracker.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.Utils.Facciones

class AdapterFaccion : RecyclerView.Adapter<AdapterFaccion.ViewHolder>() {
    var facciones: MutableList<Facciones> = ArrayList()
    lateinit var context: Context
    private lateinit var miListener: onItemClickListener


    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        miListener = listener
    }

    fun AdapterFaccion(lista: MutableList<Facciones>, context: Context){
        this.facciones = lista
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.card_facciones, parent, false), miListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val faccion = facciones[position]
        holder.bind(faccion, context)
    }

    override fun getItemCount(): Int {
        return facciones.size
    }

    class ViewHolder(view: View, listener: onItemClickListener): RecyclerView.ViewHolder(view){
        private val nombre = view.findViewById(R.id.nombreFaccion) as TextView
        private val logo = view.findViewById(R.id.logoFaccion) as ImageView

        fun bind(facciones: Facciones, context: Context){
           nombre.text = facciones.nombreFaccion
           logo.setImageResource(facciones.logoFaccion)
        }

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

}