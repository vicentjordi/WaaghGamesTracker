package com.vicentjordi.waaghgamestracker.Partidas

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.databinding.FragmentJugarPartidaBinding

class JugarPartidaFragment : Fragment() {
    private var _binding: FragmentJugarPartidaBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()

    private lateinit var guardarFaccionJ1: String
    private lateinit var guardarFaccionJ2: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentJugarPartidaBinding.inflate(inflater, container, false)

        binding.btnRestarPJ1.setOnClickListener{
            actualizarValor(binding.puntosJ1, "restar")
        }
        binding.btnSumarPJ1.setOnClickListener {
            actualizarValor(binding.puntosJ1, "sumar")
        }

        binding.btnRestarCPJ1.setOnClickListener{
            actualizarValor(binding.CPJ1, "restar")
        }
        binding.btnSumarCPJ1.setOnClickListener {
            actualizarValor(binding.CPJ1, "sumar")
        }

        binding.btnRestarPJ2.setOnClickListener {
            actualizarValor(binding.puntosJ2, "restar")
        }

        binding.btnSumarPJ2.setOnClickListener {
            actualizarValor(binding.puntosJ2, "sumar")
        }

        binding.btnRestarCPJ2.setOnClickListener {
            actualizarValor(binding.CPJ1, "restar")
        }

        binding.btnSumarCPJ2.setOnClickListener {
            actualizarValor(binding.CPJ2, "sumar")
        }

        binding.btnTerminarpartida.setOnClickListener {
            val context = requireContext()
            val prefs = context.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
            val email = prefs.getString("email", null)

            val partidaData = hashMapOf(
                "email" to email,
                "nombreJ1" to binding.J1.text.toString(),
                "puntosJ1" to binding.puntosJ1.text.toString(),
                "faccionJ1" to guardarFaccionJ1,
                "nombreJ2" to binding.J2.text.toString(),
                "puntosJ2" to binding.puntosJ2.text.toString(),
                "faccionJ2" to guardarFaccionJ2
            )

            db.collection("partidas")
                .add(partidaData)
                .addOnSuccessListener { documentReference ->
                    val fragmentPartida = PartidasFragment()
                    cargarFragments(fragmentPartida)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), getString(R.string.errorPartida), Toast.LENGTH_SHORT).show()
                }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null){
            val nombreJ1 = arguments?.getString("nombreJ1")
            val nombreJ2 = arguments?.getString("nombreJ2")
            val faccionJ1 = arguments?.getString("faccionJ1")
            val faccionJ2 = arguments?.getString("faccionJ2")
            val cpJ1 = arguments?.getInt("cpJ1")
            val cpJ2 = arguments?.getInt("cpJ2")
            val puntosJ1 = arguments?.getInt("puntosJ1")
            val puntosJ2 = arguments?.getInt("puntosJ2")

            binding.J1.text = nombreJ1.toString()
            guardarFaccionJ1 = faccionJ1.toString()
            binding.CPJ1.text = cpJ1.toString()
            binding.puntosJ1.text = puntosJ1.toString()

            binding.J2.text = nombreJ2.toString()
            guardarFaccionJ2 = faccionJ2.toString()
            binding.CPJ2.text = cpJ2.toString()
            binding.puntosJ2.text = puntosJ2.toString()
        }
    }
    fun actualizarValor(textView: TextView, operacion: String) {
        val valorActual = textView.text.toString().toInt()

        val nuevoValor = when (operacion) {
            "sumar" -> valorActual + 1
            "restar" -> valorActual - 1
            else -> valorActual // Operación inválida, mantener el mismo valor
        }

        textView.text = nuevoValor.toString()
    }
    private fun cargarFragments(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contenedorPartidaFragments, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
