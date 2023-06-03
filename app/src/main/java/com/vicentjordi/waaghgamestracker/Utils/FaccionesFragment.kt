package com.vicentjordi.waaghgamestracker.Utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.vicentjordi.waaghgamestracker.Adapters.AdapterFaccion
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.databinding.FragmentFaccionesBinding

class FaccionesFragment : Fragment() {
    private var _binding: FragmentFaccionesBinding? = null
    private val binding get() = _binding!!
    private val adaptador : AdapterFaccion = AdapterFaccion()
    private  var faccionClickListener: seleccionarFaccion? = null

    fun setFaccionClickListener(listener: seleccionarFaccion){
        faccionClickListener = listener
    }
    interface seleccionarFaccion {
        fun devolverFaccion(faccionSeleccionada: String)
    }
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

        adaptador.setOnItemClickListener(object : AdapterFaccion.onItemClickListener {
            override fun onItemClick(position: Int) {
                val fragmentManager = requireActivity().supportFragmentManager
                val faccion = crearListaFaccion()[position]
                val nomFaccion = faccion.nombreFaccion
                faccionClickListener?.devolverFaccion(nomFaccion)
                fragmentManager.popBackStack()
            }
        })
        return binding.root
    }

    private fun crearListaFaccion(): MutableList<Facciones> {
        val faccion: MutableList<Facciones> = arrayListOf()
        faccion.add(Facciones(getString(R.string.templarios),R.drawable.logo_templarios))
        faccion.add(Facciones(getString(R.string.sangrientos),R.drawable.logo_sangrientos))
        faccion.add(Facciones(getString(R.string.oscuros),R.drawable.logo_oscuros))
        faccion.add(Facciones(getString(R.string.vigias),R.drawable.logo_vigias))
        faccion.add(Facciones(getString(R.string.caballeros),R.drawable.logo_caballeros))
        faccion.add(Facciones(getString(R.string.punyos),R.drawable.logo_punyos))
        faccion.add(Facciones(getString(R.string.manos),R.drawable.logo_manos))
        faccion.add(Facciones(getString(R.string.guardia),R.drawable.logo_guardia))
        faccion.add(Facciones(getString(R.string.salamandras),R.drawable.logo_salamandras))
        faccion.add(Facciones(getString(R.string.lobos),R.drawable.logo_lobos))
        faccion.add(Facciones(getString(R.string.ultramarines), R.drawable.logo_ultramarines))
        faccion.add(Facciones(getString(R.string.cicatrices),R.drawable.logo_cicatrices))
        faccion.add(Facciones(getString(R.string.sororitas), R.drawable.logo_sororitas))
        faccion.add(Facciones(getString(R.string.custodes),R.drawable.logo_custodes))
        faccion.add(Facciones(getString(R.string.mechanicus),R.drawable.logo_mechanicus))
        faccion.add(Facciones(getString(R.string.militarum),R.drawable.logo_militarum))
        faccion.add(Facciones(getString(R.string.imperiales),R.drawable.logo_imperiales))
        faccion.add(Facciones(getString(R.string.inquisidores),R.drawable.logo_inquisidores))
        faccion.add(Facciones(getString(R.string.assassinorum),R.drawable.logo_assassinorum))
        faccion.add(Facciones(getString(R.string.demonios),R.drawable.logo_caos))
        faccion.add(Facciones(getString(R.string.marinesCaos),R.drawable.logo_imperialescaos))
        faccion.add(Facciones(getString(R.string.guardiaMuerte),R.drawable.logo_guardiamuerte))
        faccion.add(Facciones(getString(R.string.milHijos),R.drawable.logo_milhijos))
        faccion.add(Facciones(getString(R.string.devoradores),R.drawable.logo_devoradores))
        faccion.add(Facciones(getString(R.string.aeldar),R.drawable.logo_aeldar))
        faccion.add(Facciones(getString(R.string.drukhari),R.drawable.logo_drukhari))
        faccion.add(Facciones(getString(R.string.genestealer),R.drawable.logo_genestealer))
        faccion.add(Facciones(getString(R.string.tiranidos),R.drawable.logo_tiranidos))
        faccion.add(Facciones(getString(R.string.necornes),R.drawable.logo_necrons))
        faccion.add(Facciones(getString(R.string.tau),R.drawable.logo_tau))
        faccion.add(Facciones(getString(R.string.orcos),R.drawable.logo_orks))

        return faccion
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}