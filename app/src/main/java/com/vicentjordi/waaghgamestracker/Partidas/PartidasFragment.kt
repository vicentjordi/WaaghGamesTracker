package com.vicentjordi.waaghgamestracker.Partidas

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vicentjordi.waaghgamestracker.Listas.CrearListaActivity
import com.vicentjordi.waaghgamestracker.databinding.FragmentPartidasBinding

class PartidasFragment : Fragment() {
    private var _binding: FragmentPartidasBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPartidasBinding.inflate(inflater, container, false)

        binding.btnNuevaPartida.setOnClickListener {
            crearPartida()
        }

        return binding.root
    }

    private fun crearPartida(){
        val context = activity
        val crearPartidaIntent = Intent(context, CrearPartidaActivity::class.java)
        startActivity(crearPartidaIntent)
    }

}