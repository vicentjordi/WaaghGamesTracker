package com.vicentjordi.waaghgamestracker.Listas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.vicentjordi.waaghgamestracker.R
import com.vicentjordi.waaghgamestracker.databinding.ActivityCrearListaBinding


class CrearListaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCrearListaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearListaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fragmentInci()
    }

    private fun fragmentInci(){
        val primerFragment = GenerarListaFragment()
        val pm: FragmentManager = supportFragmentManager
        pm.beginTransaction().add(R.id.contenedorListasFragments,primerFragment).commit()
    }
}