package com.vicentjordi.waaghgamestracker.Inicio
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vicentjordi.waaghgamestracker.Listas.ListasFragment
import com.vicentjordi.waaghgamestracker.Partidas.PartidasFragment
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.databinding.FragmentInicioBinding


class InicioFragment : Fragment() {
    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

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

        return binding.root
    }

    private fun cargarFragments(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contenedorFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}