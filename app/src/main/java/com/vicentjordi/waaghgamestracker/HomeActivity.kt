package com.vicentjordi.waaghgamestracker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.vicentjordi.waaghgamestracker.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.vicentjordi.waaghgamestracker.Listas.ListasFragment
import com.vicentjordi.waaghgamestracker.Inicio.InicioFragment
import com.vicentjordi.waaghgamestracker.Partidas.PartidasFragment

enum class ProviderType{
    BASIC,
    GOOGLE
}
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup()
        val bundle:Bundle? = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email?: "", provider?: "")

        //Después de Iniciar sesión se guardan los datos en un archivo local, para mantener la sesión del usuario.
        //En caso de cerrar la aplicación siempre y cuando no se cierre sesión el usuario permanecerá activo.
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()

        fragmentInci()

        //Menu Lateral
        toolbar = binding.toolbar
        drawerLayout = binding.drawerLayout
        navigationView = binding.menuLateral

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.cerrar)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        toggle.drawerArrowDrawable?.color = ContextCompat.getColor(this,R.color.white)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.inicioFragment ->{
                    val fragmentIncio = InicioFragment()
                    cargarFragments(fragmentIncio)
                    toolbar.title = getString(R.string.menuInico)
                    true
                }
                R.id.listasFragment ->{
                    val fragmentoListas = ListasFragment()
                    cargarFragments(fragmentoListas)
                    toolbar.title = getString(R.string.menuListas)
                    true
                }
                R.id.partidasFragment ->{
                    val fragmentoPartidas = PartidasFragment()
                    cargarFragments(fragmentoPartidas)
                    toolbar.title = getString(R.string.menuPartidas)
                    true
                }
                R.id.salir ->{
                    //Se cierra sesíon y se vacia el archivo donde esta la información del usuario activo.
                    FirebaseAuth.getInstance().signOut()
                    prefs.clear()
                    prefs.apply()
                    val menuPrincipal = Intent(this, MainActivity::class.java)
                    startActivity(menuPrincipal)
                    true
                }
                else -> false
            }
        }


    }

    private fun setup(email: String, provider: String){
      //  val username = findViewById<TextView>(R.id.userNameMenu)
       // useremail.text = email
       // username.text = provider
    }

    private fun cargarFragments(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.contenedorFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun fragmentInci(){
        val primerFragment = InicioFragment()
        val pm: FragmentManager = supportFragmentManager
        pm.beginTransaction().add(R.id.contenedorFragment,primerFragment).commit()
    }
}