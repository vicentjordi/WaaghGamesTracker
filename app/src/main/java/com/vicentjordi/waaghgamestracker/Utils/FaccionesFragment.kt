package com.vicentjordi.waaghgamestracker.Utils

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.vicentjordi.waaghgamestracker.Adapters.AdapterFaccion
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.databinding.FragmentFaccionesBinding
import com.vicentjordi.waaghgamestracker.databinding.FragmentInciarPartidaBinding

class FaccionesFragment : Fragment() {
    private var _binding: FragmentFaccionesBinding? = null
    private val binding get() = _binding!!
    private val adaptador : AdapterFaccion = AdapterFaccion()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFaccionesBinding.inflate(inflater, container, false)

        binding.rvFacciones.setHasFixedSize(true)
        binding.rvFacciones.layoutManager = LinearLayoutManager(requireContext())
        adaptador.AdapterFaccion(crearListaFaccion(), requireContext())
        binding.rvFacciones.adapter = adaptador

        return  binding.root
    }

    private fun crearListaFaccion(): MutableList<Facciones> {
        val faccion: MutableList<Facciones> = arrayListOf()
        faccion.add(Facciones("Necrones", R.drawable.necrons))
        return faccion
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}