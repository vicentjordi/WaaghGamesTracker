package com.vicentjordi.waaghgamestracker.Inicio
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import com.vicentjordi.waaghgamestracker.Adapters.AdapterListaEjercitos
import com.vicentjordi.waaghgamestracker.Adapters.AdapterResultadoPartida
import com.vicentjordi.waaghgamestracker.Listas.ListasFragment
import com.vicentjordi.waaghgamestracker.Partidas.PartidasFragment
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.Utils.ListaEjercitos
import com.vicentjordi.waaghgamestracker.Utils.ResultadoPartida
import com.vicentjordi.waaghgamestracker.databinding.FragmentInicioBinding


class InicioFragment : Fragment() {
    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!
    private val adaptadorPartida : AdapterResultadoPartida = AdapterResultadoPartida()
    private val adaptadorEjercitos : AdapterListaEjercitos = AdapterListaEjercitos()
    private val db = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)

        binding.ListasMinis.setOnClickListener {
            val fragmentoListas = ListasFragment()
            cargarFragments(fragmentoListas)
        }

        binding.PartidasHechasIncio.setOnClickListener {
            val fragmentoPartida = PartidasFragment()
            cargarFragments(fragmentoPartida)

        }
        adaptadorRvPartida()
        adaptadorRvEjercitos()

        return binding.root
    }
    private fun cargarFragments(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contenedorFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun adaptadorRvEjercitos(){
        binding.rvListasInico.setHasFixedSize(true)
        binding.rvListasInico.layoutManager = LinearLayoutManager(requireContext())
        adaptadorEjercitos.AdapterListaEjercitos(crearListaEjercitos(), requireContext())
        binding.rvListasInico.adapter = adaptadorEjercitos

        adaptadorEjercitos.setOnItemClickListener(object: AdapterListaEjercitos.OnItemClickListener{
            override fun onItemClick(position: Int, id: String) {
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar Partida")
                    .setMessage("Estas seguro de eliminar la Partida?")
                    .setPositiveButton("Acepar") { dialog, _ ->
                       // eliminarRegistroPartida(id, position)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                alertDialog.show()
            }
        })
    }
    private fun adaptadorRvPartida(){
        binding.rvPartidasinico.setHasFixedSize(true)
        binding.rvPartidasinico.layoutManager = LinearLayoutManager(requireContext())
        adaptadorPartida.AdapterResultadoPartida(crearListaResultado(), requireContext())
        binding.rvPartidasinico.adapter = adaptadorPartida

        adaptadorPartida.setOnItemClickListener(object : AdapterResultadoPartida.onItemClickListener{
            override fun onItemClick(position: Int, id: String, email: String) {
                val alertDialog = AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar Partida")
                    .setMessage("Estas seguro de eliminar la Partida?")
                    .setPositiveButton("Acepar") { dialog, _ ->
                        eliminarRegistroPartida(id, position)
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                alertDialog.show()
            }
        })

    }
    private fun eliminarRegistroPartida(id: String, position: Int){
        db.collection("partidas")
            .document(id)
            .delete()
            .addOnSuccessListener{
                Toast.makeText(requireContext(), "La partida se ha eliminado correctamente", Toast.LENGTH_SHORT).show()
                adaptadorPartida.partidas.removeAt(position)
                adaptadorPartida.notifyItemRemoved(position)
            }.addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Ha ocurrido algun problema, y no se ha podido eliminar la partida", Toast.LENGTH_SHORT).show()
            }
    }
    private fun crearListaResultado(): MutableList<ResultadoPartida> {
        val partida: MutableList<ResultadoPartida> = mutableListOf()

        // Obtener el email del SharedPreferences
        val context = requireContext()
        val prefs = context.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)

        if (email != null) {
            obtenerPartidasPorEmail(email) { partidasList ->
                partida.addAll(partidasList)
                adaptadorPartida.notifyDataSetChanged()
            }
        }

        return partida
    }
    private fun obtenerPartidasPorEmail(email: String, callback: (List<ResultadoPartida>) -> Unit) {
        db.collection("partidas")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val partidasList = mutableListOf<ResultadoPartida>()

                for (document in querySnapshot) {
                    val id = document.id
                    val email = document.getString("email") ?: ""
                    val nombreJ1 = document.getString("nombreJ1") ?: ""
                    val puntosJ1 = document.getString("puntosJ1") ?: ""
                    val faccionJ1 = document.getString("faccionJ1") ?: ""
                    val nombreJ2 = document.getString("nombreJ2") ?: ""
                    val puntosJ2 = document.getString("puntosJ2") ?: ""
                    val faccionJ2 = document.getString("faccionJ2") ?: ""

                    val partida = ResultadoPartida(id, email, nombreJ1, puntosJ1, faccionJ1, nombreJ2, puntosJ2, faccionJ2)
                    partidasList.add(partida)
                }

                callback(partidasList)
            }
            .addOnFailureListener { e ->
                // Manejar el error según sea necesario
            }
    }
    fun crearListaEjercitos(): MutableList<ListaEjercitos>{
        val listas: MutableList<ListaEjercitos> = mutableListOf()

        // Obtener el email del SharedPreferences
        val context = requireContext()
        val prefs = context.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)

        if (email != null) {
            obtenerListaEjercitosPorEmail(email) { listaEjercitos ->
                listas.addAll(listaEjercitos)
                adaptadorEjercitos.notifyDataSetChanged()
            }
        }

        return listas
    }
    fun obtenerListaEjercitosPorEmail(email: String, callback: (List<ListaEjercitos>) -> Unit){
        db.collection("listas")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val listaEjercito = mutableListOf<ListaEjercitos>()

                for (document in querySnapshot) {
                    val id = document.id
                    val email = document.getString("email") ?: ""
                    val faccion = document.getString("faccion") ?: ""
                    val nombreLista = document.getString("nombreLista") ?: ""
                    val tipoLista = document.getString("tipoLista") ?: ""



                    val lista = ListaEjercitos(id, email, nombreLista, faccion, tipoLista)
                    listaEjercito.add(lista)
                }

                callback(listaEjercito)
            }
            .addOnFailureListener { e ->
                // Manejar el error según sea necesario
            }
    }

}