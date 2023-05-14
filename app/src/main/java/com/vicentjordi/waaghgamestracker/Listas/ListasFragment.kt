package com.vicentjordi.waaghgamestracker.Listas

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.vicentjordi.waaghgamestracker.HomeActivity
import com.vicentjordi.waaghgamestracker.Inicio.InicioFragment
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.databinding.FragmentListasBinding

class ListasFragment : Fragment() {
    private var _binding: FragmentListasBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListasBinding.inflate(inflater, container, false)

        binding.btnNuevaLista.setOnClickListener {
            crearLista()
        }

        return binding.root
    }

    private fun crearLista(){
        val context = activity
        val crearListaIntent = Intent(context, CrearListaActivity::class.java)
        startActivity(crearListaIntent)
    }

}