package com.vicentjordi.waaghgamestracker.Listas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.vicentjordi.waaghgamestracker.Adapters.AdapterMini
import com.vicentjordi.waaghgamestracker.Utils.Minis
import com.vicentjordi.waaghgamestracker.databinding.FragmentBuscarMiniBinding

class BuscarMiniFragment : Fragment() {
    private var _binding: FragmentBuscarMiniBinding? = null
    private val binding get() = _binding!!
    private val adaptador : AdapterMini = AdapterMini()
    private lateinit var faccion: String
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBuscarMiniBinding.inflate(inflater, container, false)

        if (arguments != null){
            faccion = arguments?.getString("faccion").toString()
        }

        binding.rvListaMinis.setHasFixedSize(true)
        binding.rvListaMinis.layoutManager = LinearLayoutManager(requireContext())
        adaptador.AdapterMini(crearListadoMini(), requireContext())
        binding.rvListaMinis.adapter = adaptador

        return binding.root
    }

    private fun crearListadoMini(): MutableList<Minis>{
        val mini: MutableList<Minis> = mutableListOf()
        if (faccion != null){
            obtenerMinis(faccion){ minisList ->
                mini.addAll(minisList)
                adaptador.notifyDataSetChanged()
            }
        }

        return mini
    }

    private fun obtenerMinis(faccion: String, callback: (List<Minis>) -> Unit){
        val spaceMarines = listOf("Templarios Negros", "Ángeles Sangrientos", "Ángeles Oscuros", "Vigías de la muerte",
            "Caballeros Grises", "Puños Imperiales", "Manos de Hierro", "Guardia del Cuervo", "Salamandras", "Lobos Espaciales",
            "Ultramarines", "Cicatrices Blancas")

        if (spaceMarines.contains(faccion)){
            db.collection("miniaturas")
                .whereIn("faccion", listOf(faccion, "Marines Espaciales"))
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val minisList = mutableListOf<Minis>()

                    for (document in querySnapshot) {
                        val nombreMini = document.getString("nombre") ?: ""
                        val faccionMini = document.getString("faccion") ?: ""
                        val cantidadMini = document.getString("tamaño") ?: ""
                        val costeMiniLong = document.getLong("coste") ?: 0
                        val costeMini = costeMiniLong.toInt()

                        val miniatura = Minis(nombreMini, faccionMini, cantidadMini, costeMini.toInt())
                        minisList.add(miniatura)
                    }

                    callback(minisList)
                }
                .addOnFailureListener { e ->
                    // Manejar el error según sea necesario
                }
        }else{
            db.collection("miniaturas")
                .whereEqualTo("faccion", faccion)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val minisList = mutableListOf<Minis>()

                    for (document in querySnapshot) {
                        val nombreMini = document.getString("nombre") ?: ""
                        val faccionMini = document.getString("faccion") ?: ""
                        val cantidadMini = document.getString("tamaño") ?: ""
                        val costeMiniLong = document.getLong("coste") ?: 0
                        val costeMini = costeMiniLong.toInt()

                        val miniatura = Minis(nombreMini, faccionMini, cantidadMini, costeMini.toInt())
                        minisList.add(miniatura)
                    }

                    callback(minisList)
                }
                .addOnFailureListener { e ->
                    // Manejar el error según sea necesario
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}