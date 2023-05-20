package com.vicentjordi.waaghgamestracker.Partidas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vicentjordi.waaghgamestracker.databinding.FragmentJugarPartidaBinding

class JugarPartidaFragment : Fragment() {
    private var _binding: FragmentJugarPartidaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentJugarPartidaBinding.inflate(inflater, container, false)

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

            binding.J1.text = nombreJ1
            binding.CPJ1.text = cpJ1.toString()
            binding.puntosJ1.text = puntosJ1.toString()

            binding.J2.text = nombreJ2
            binding.CPJ2.text = cpJ2.toString()
            binding.puntosJ2.text = puntosJ2.toString()
        }
    }

}
