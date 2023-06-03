package com.vicentjordi.waaghgamestracker.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.Utils.ResultadoPartida

class AdapterResultadoPartida : RecyclerView.Adapter<AdapterResultadoPartida.ViewHolder>() {
    var partidas: MutableList<ResultadoPartida> = ArrayList()
    lateinit var context: Context
    private lateinit var miListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int, id: String, email: String)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        miListener = listener
    }

    fun AdapterResultadoPartida(lista: MutableList<ResultadoPartida>, context: Context) {
        this.partidas = lista
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.partida_card, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val partida = partidas[position]
        holder.bind(partida, context)
    }

    override fun getItemCount(): Int {
        return partidas.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nombreJ1 = view.findViewById(R.id.resnombreJ1) as TextView
        private val faccionJ1 = view.findViewById(R.id.resfaccionJ1) as TextView
        private val puntosJ1 = view.findViewById(R.id.respuntosJ1) as TextView
        private val nombreJ2 = view.findViewById(R.id.resnombreJ2) as TextView
        private val faccionJ2 = view.findViewById(R.id.resfaccionJ2) as TextView
        private val puntosJ2 = view.findViewById(R.id.respuntosJ2) as TextView

        fun bind(partida: ResultadoPartida, context: Context) {
            nombreJ1.text = partida.nombreJ1
            faccionJ1.text = partida.faccionJ1
            puntosJ1.text = partida.puntosJ1

            nombreJ2.text = partida.nombreJ2
            faccionJ2.text = partida.faccionJ2
            puntosJ2.text = partida.puntosJ2

            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val id = partidas[position].id
                    val email = partidas[position].email
                    miListener.onItemClick(position, id, email)
                    true
                } else {
                    false
                }
            }
        }
    }
}

