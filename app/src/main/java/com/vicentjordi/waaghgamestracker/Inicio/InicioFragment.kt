package com.vicentjordi.waaghgamestracker.Inicio
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch
import com.vicentjordi.waaghgamestracker.Adapters.AdapterResultadoPartida
import com.vicentjordi.waaghgamestracker.Listas.ListasFragment
import com.vicentjordi.waaghgamestracker.Partidas.PartidasFragment
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.Utils.ResultadoPartida
import com.vicentjordi.waaghgamestracker.databinding.FragmentInicioBinding


class InicioFragment : Fragment() {
    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!
    private val adaptadorPartida : AdapterResultadoPartida = AdapterResultadoPartida()
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

        return binding.root
    }
    private fun cargarFragments(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contenedorFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    private fun adaptadorRvPartida(){
        binding.rvPartidasinico.setHasFixedSize(true)
        binding.rvPartidasinico.layoutManager = LinearLayoutManager(requireContext())
        adaptadorPartida.AdapterResultadoPartida(crearListaResultado(), requireContext())
        binding.rvPartidasinico.adapter = adaptadorPartida
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
                    val nombreJ1 = document.getString("nombreJ1") ?: ""
                    val puntosJ1 = document.getString("puntosJ1") ?: ""
                    val faccionJ1 = document.getString("faccionJ1") ?: ""
                    val nombreJ2 = document.getString("nombreJ2") ?: ""
                    val puntosJ2 = document.getString("puntosJ2") ?: ""
                    val faccionJ2 = document.getString("faccionJ2") ?: ""

                    val partida = ResultadoPartida(nombreJ1, puntosJ1, faccionJ1, nombreJ2, puntosJ2, faccionJ2)
                    partidasList.add(partida)
                }

                callback(partidasList)
            }
            .addOnFailureListener { e ->
                // Manejar el error según sea necesario
            }
    }
}