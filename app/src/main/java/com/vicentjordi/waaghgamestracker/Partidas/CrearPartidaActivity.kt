package com.vicentjordi.waaghgamestracker.Partidas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.vicentjordi.waaghgamestracker.Inicio.InicioFragment
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.databinding.ActivityCrearPartidaBinding
import com.vicentjordi.waaghgamestracker.databinding.ActivityHomeBinding

class CrearPartidaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCrearPartidaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearPartidaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentInci()
    }

    private fun fragmentInci(){
        val primerFragment = InciarPartidaFragment()
        val pm: FragmentManager = supportFragmentManager
        pm.beginTransaction().add(R.id.contenedorPartidaFragments,primerFragment).commit()
    }
}