package com.vicentjordi.waaghgamestracker.Partidas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.vicentjordi.waaghgamestracker.Adapters.AdapterFaccion
import com.vicentjordi.waaghgamestracker.Adapters.AdapterResultadoPartida
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.Utils.FaccionesFragment
import com.vicentjordi.waaghgamestracker.databinding.FragmentInciarPartidaBinding

class InciarPartidaFragment : Fragment(), FaccionesFragment.seleccionarFaccion{
    private var _binding: FragmentInciarPartidaBinding? = null
    private val binding get() = _binding!!
    private var faccionSeleccionadaPor: Int = 0
    private var faccion: String? = null
    private var faccion2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInciarPartidaBinding.inflate(inflater, container, false)

        binding.faccionJ1.setOnClickListener {
            faccionSeleccionadaPor = 1
            val fragmentFaccion = FaccionesFragment()
            fragmentFaccion.setFaccionClickListener(this@InciarPartidaFragment)
            cargarFragments(fragmentFaccion)
        }

        binding.faccionJ2.setOnClickListener {
            faccionSeleccionadaPor = 2
            val fragmentFaccion = FaccionesFragment()
            fragmentFaccion.setFaccionClickListener(this@InciarPartidaFragment)
            cargarFragments(fragmentFaccion)
        }

        binding.btnEmpezarPartida.setOnClickListener {
            empezarPartida()
        }

        return binding.root
    }

    private fun empezarPartida(){
        var error: Boolean
        var nombreJ1: String
        var nombreJ2: String
        var cpJ1: Int
        var cpJ2: Int
        var faccionJ1: String
        var faccionJ2: String
        var puntosJ1: Int
        var puntosJ2: Int
        val fragment = JugarPartidaFragment()
        val bundle = Bundle()

        error = false
        cpJ1 = 0
        cpJ2 = 0
        faccionJ1 = ""
        faccionJ2 = ""

        if (binding.faccionJ1.text == getString(R.string.faccion)){
            //errorFaccion()
            //error = true
            faccionJ2 = binding.faccionJ2.text.toString()
        }else{
           faccionJ1 = binding.faccionJ1.text.toString()
        }


        if (binding.faccionJ2.text == getString(R.string.faccion)){
            //errorFaccion()
            //error = true
            faccionJ2 = binding.faccionJ2.text.toString()
        }else{
            faccionJ2 = binding.faccionJ2.text.toString()
        }

        if (binding.edPuntosComandoJ1.text.isEmpty()){
            cpJ1 = 10
        }else{
            val stringcp = binding.edPuntosComandoJ1.text.toString()
            try {
                cpJ1 = stringcp.toInt()
            }catch (e: java.lang.NumberFormatException){
                errorCP()
            }
        }

        if (binding.edPuntosComandoJ2.text.isEmpty()){
            cpJ2 = 10
        }else{
            val stringcp = binding.edPuntosComandoJ2.text.toString()
            try {
                cpJ2 = stringcp.toInt()
            }catch (e: java.lang.NumberFormatException){
                errorCP()
            }
        }

        if (binding.cbJ1.isChecked){
            puntosJ1 = 10
        }else{
            puntosJ1 = 0
        }

        if (binding.cbJ2.isChecked){
            puntosJ2 = 10
        }else{
            puntosJ2 = 0
        }

        if (binding.edNombreJ1.text.isEmpty()){
            nombreJ1 = "Jugador 1"
        }else{
            nombreJ1 = binding.edNombreJ1.text.toString()
        }

        if (binding.edNombreJ2.text.isEmpty()){
            nombreJ2 = "Jugador 1"
        }else{
            nombreJ2 = binding.edNombreJ2.text.toString()
        }

        if (error == false){
            bundle.putString("nombreJ1", nombreJ1)
            bundle.putString("nombreJ2", nombreJ2)
            bundle.putString("faccionJ1", faccionJ1)
            bundle.putString("faccionJ2", faccionJ2)
            bundle.putInt("cpJ1", cpJ1)
            bundle.putInt("cpJ2", cpJ2)
            bundle.putInt("puntosJ1", puntosJ1)
            bundle.putInt("puntosJ2", puntosJ2)

            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.contenedorPartidaFragments, fragment)
            fragment.arguments = bundle
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private fun cargarFragments(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contenedorPartidaFragments, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun errorCP(){
        Toast.makeText(activity,
            R.string.errorCP,
            Toast.LENGTH_SHORT).show()
    }

    private fun errorFaccion(){
        Toast.makeText(activity,
            R.string.errorFaccion,
            Toast.LENGTH_SHORT).show()
    }
    override fun devolverFaccion(faccionSeleccionada: String) {
        //Asegura que se realize la actualizacion del textView en el hilo Principal.
        requireActivity().runOnUiThread{
            if (faccionSeleccionadaPor == 1)
                faccion = faccionSeleccionada
            else if (faccionSeleccionadaPor == 2)
                faccion2 = faccionSeleccionada
        }
    }

    override fun onResume() {
        super.onResume()
        when (faccionSeleccionadaPor) {
            1 -> {
                binding.faccionJ1.text = faccion
                if (faccion2 == null){
                    binding.faccionJ2.text = "faccionJ1"
                }else{
                    binding.faccionJ2.text = faccion2
                }
            }
            2 -> {
                if (faccion == null){
                    binding.faccionJ1.text = "faccionJ2"
                }else{
                    binding.faccionJ1.text = faccion
                }
                binding.faccionJ2.text = faccion2
            }
        }
    }

}