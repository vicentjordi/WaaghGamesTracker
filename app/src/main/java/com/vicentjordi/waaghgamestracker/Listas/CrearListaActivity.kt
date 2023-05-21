package com.vicentjordi.waaghgamestracker.Listas

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.vicentjordi.waaghgamestracker.R

class CrearListaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_lista)

        session()
    }

    private fun session(){
        //Cargar Datos del usuario logeado
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if(email != null && provider != null){
            Toast.makeText(this, "Sessio de l'usuari amb l'email {$email}", Toast.LENGTH_SHORT).show()
        }
    }
}