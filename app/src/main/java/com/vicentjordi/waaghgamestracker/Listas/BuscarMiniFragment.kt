package com.vicentjordi.waaghgamestracker.Listas

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.vicentjordi.waaghgamestracker.Adapters.AdapterMini
import com.vicentjordi.waaghgamestracker.Adapters.AdapterResultadoPartida
import com.vicentjordi.waaghgamestracker.Utils.Minis
import com.vicentjordi.waaghgamestracker.databinding.FragmentBuscarMiniBinding

class BuscarMiniFragment : Fragment() {
    private var _binding: FragmentBuscarMiniBinding? = null
    private val binding get() = _binding!!
    private val adaptador : AdapterMini = AdapterMini()
    private lateinit var faccion: String
    private lateinit var idLista: String
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBuscarMiniBinding.inflate(inflater, container, false)

        faccion = arguments?.getString("faccion")?: ""
        idLista = arguments?.getString("idLista")?: ""

        Toast.makeText(requireContext(), "$faccion", Toast.LENGTH_SHORT).show()
        Toast.makeText(requireContext(), "$idLista", Toast.LENGTH_SHORT).show()

        binding.rvListaMinis.setHasFixedSize(true)
        binding.rvListaMinis.layoutManager = LinearLayoutManager(requireContext())
        adaptador.AdapterMini(crearListadoMini(), requireContext())
        binding.rvListaMinis.adapter = adaptador
        adaptador.setOnItemClickListener(object : AdapterMini.onItemClickListener{
            override fun onItemClick(position: Int, id: String) {
                val fragmentManager = requireActivity().supportFragmentManager
                agregarMini(id)
                fragmentManager.popBackStack()
            }
        })
        return binding.root
    }

    private fun agregarMini(id: String) {
        val miniaturaSeleccionada = adaptador.mini.firstOrNull { it.id == id }

        if (miniaturaSeleccionada != null) {
            val miniaturaMap = hashMapOf(
                "id" to miniaturaSeleccionada.id,
                "nombreMini" to miniaturaSeleccionada.nombreMini,
                "faccionMini" to miniaturaSeleccionada.faccionMini,
                "tamaño" to miniaturaSeleccionada.tamaño,
                "coste" to miniaturaSeleccionada.coste
            )

            db.collection("listas")
                .document(idLista)
                .update("listaMini", FieldValue.arrayUnion(miniaturaMap))
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Miniatura agregada correctamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show()
                }
        }
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
                        val id = document.id
                        val nombreMini = document.getString("nombre") ?: ""
                        val faccionMini = document.getString("faccion") ?: ""
                        val cantidadMini = document.getString("tamaño") ?: ""
                        val costeMiniLong = document.getLong("coste") ?: 0
                        val costeMini = costeMiniLong.toInt()

                        val miniatura = Minis(id, nombreMini, faccionMini, cantidadMini, costeMini.toInt())
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
                        val id = document.id
                        val nombreMini = document.getString("nombre") ?: ""
                        val faccionMini = document.getString("faccion") ?: ""
                        val cantidadMini = document.getString("tamaño") ?: ""
                        val costeMiniLong = document.getLong("coste") ?: 0
                        val costeMini = costeMiniLong.toInt()

                        val miniatura = Minis(id, nombreMini, faccionMini, cantidadMini, costeMini.toInt())
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