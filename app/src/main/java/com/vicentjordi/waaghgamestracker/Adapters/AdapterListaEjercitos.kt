package com.vicentjordi.waaghgamestracker.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.Utils.ListaEjercitos
import com.vicentjordi.waaghgamestracker.Utils.ResultadoPartida

class AdapterListaEjercitos : RecyclerView.Adapter<AdapterListaEjercitos.ViewHolder>() {
    var ejercitos: MutableList<ListaEjercitos> = ArrayList()
    lateinit var context: Context
    private lateinit var miListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int, id: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        miListener = listener
    }

    fun AdapterListaEjercitos(lista: MutableList<ListaEjercitos>, context: Context) {
        this.ejercitos = lista
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.listaejercito_card, parent, false), miListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ejercito = ejercitos[position]
        holder.bind(ejercito, context)
    }

    override fun getItemCount(): Int {
        return ejercitos.size
    }

    inner class ViewHolder(
        view: View,
        private val listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(view) {
        private val nombreLista = view.findViewById(R.id.txtNombreLista) as TextView
        private val faccionLista = view.findViewById(R.id.txtFaccionLista) as TextView
        private val puntosLista = view.findViewById(R.id.txtPuntosLista) as TextView
        private val etiquietaPuntos = view.findViewById(R.id.etiquetaPuntosLista) as TextView

        fun bind(lista: ListaEjercitos, context: Context) {
            nombreLista.text = lista.nombreLista
            faccionLista.text = lista.faccion

            itemView.setOnClickListener {
                val position = adapterPosition
                val id = ejercitos[position].id
                listener.onItemClick(position, id)
            }

            /*
             itemView.setOnLongClickListener {
                val position = adapterPosition
                val id = ejercitos[position].id
                listener.onItemClick(position, id)
                true
            }
             */

        }
    }
}

