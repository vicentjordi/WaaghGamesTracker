package com.vicentjordi.waaghgamestracker.Utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.vicentjordi.waaghgamestracker.Adapters.AdapterFaccion
import com.vicentjordi.waaghgamestracker.Partidas.CrearPartidaActivity
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.databinding.FragmentFaccionesBinding

class FaccionesFragment : Fragment() {
    private var _binding: FragmentFaccionesBinding? = null
    private val binding get() = _binding!!
    private val adaptador : AdapterFaccion = AdapterFaccion()
    private var faccionClickListener: AdapterView.OnItemClickListener? = null


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

        adaptador.setOnItemClickListener(object : AdapterFaccion.onItemClickListener{
            override fun onItemClick(position: Int) {
                val faccion = crearListaFaccion()[position]
                val nomFaccion = faccion.nombreFaccion
                Toast.makeText( requireContext(),"Faccion $nomFaccion", Toast.LENGTH_SHORT).show()
            }
        })
        return  binding.root
    }

    private fun crearListaFaccion(): MutableList<Facciones> {
        val faccion: MutableList<Facciones> = arrayListOf()
        faccion.add(Facciones("Adeptas Sororitas", R.drawable.adepta_sororitas))
        faccion.add(Facciones("Ultramarines", R.drawable.ultramarines))
        faccion.add(Facciones("Necrones", R.drawable.necrons))
        faccion.add(Facciones("Orcos", R.drawable.orks))
        return faccion
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}