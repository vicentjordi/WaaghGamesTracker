package com.vicentjordi.waaghgamestracker.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.Utils.Minis

class AdapterMini : RecyclerView.Adapter<AdapterMini.ViewHolder>() {
    var mini: MutableList<Minis> = ArrayList()
    lateinit var context: Context
    private lateinit var miListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int, id: String)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        miListener = listener
    }

    fun AdapterMini(lista: MutableList<Minis>, context: Context) {
        this.mini = lista
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.mini_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val miniatura = mini[position]
        holder.bind(miniatura, context)
    }

    override fun getItemCount(): Int {
        return mini.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nombreMini = view.findViewById(R.id.txtNombreMini) as TextView
        private val cantidadMini = view.findViewById(R.id.txtCantidadMinis) as TextView
        private val puntosMini = view.findViewById(R.id.txtPuntosMini) as TextView
        private val etiquetaPuntos = view.findViewById(R.id.etiquetaPuntos) as TextView
        private val etiquetaUnidades = view.findViewById(R.id.etiquetaUnidades) as TextView

        fun bind(miniatura: Minis, context: Context) {
            nombreMini.text = miniatura.nombreMini
            cantidadMini.text = miniatura.tamaño
            puntosMini.text = miniatura.coste.toString()

            itemView.setOnClickListener {
                val position = adapterPosition
                val id = mini[position].id
                miListener.onItemClick(position, id)
            }
        }
    }
}
