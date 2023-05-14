package com.vicentjordi.waaghgamestracker

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.core.view.isVisible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.util.ArrayUtils
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.vicentjordi.waaghgamestracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val GOOGLE_SING_IN = registerForActivityResult(StartActivityForResult()){ activityResult ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
        try {
            val account = task.getResult(ApiException::class.java)

            if (account != null){
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                    if (it.isSuccessful){
                        showHome(account.email ?: "", ProviderType.GOOGLE)
                    }else{
                        errorRegistro()
                    }
                }
            }
        } catch (e: ApiException){
            Log.e("GoogleApi", "${e.message}")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.singUp.setOnClickListener{
            opcionSingUp()
        }
        binding.login.setOnClickListener{
            opcionLogin()
        }
        binding.googleBoto.setOnClickListener{
            googleApi()
        }

        //Evento Firebase Analytics
        val analytics:FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","Integración de FireBase Completa")
        analytics.logEvent("Login", bundle)

        //SetUp
        setup()
        session()


    }
    override fun onStart() {
        super.onStart()
        binding.layoutPrincipal.visibility = View.VISIBLE
    }
    private fun setup(){
        title = "Autenticación"

        binding.btnlogin.setOnClickListener{
            if (binding.layoutLogIn.isVisible){
                iniciarSesion()
            }else{
                registrarse()
            }
        }
    }
    private fun opcionLogin(){
        binding.btnlogin.setText(R.string.btnLogin)
        binding.login.background = resources.getDrawable(R.drawable.switch_tricks, null)
        binding.login.setTextColor(resources.getColor(R.color.titulos, null))
        binding.singUp.background = null
        binding.singUp.setTextColor(resources.getColor(R.color.botones, null))
        binding.layoutLogIn.visibility = View.VISIBLE
        binding.layoutSingUp.visibility = View.GONE
    }
    private fun opcionSingUp(){
        binding.btnlogin.setText(R.string.btnRegister)
        binding.singUp.background = resources.getDrawable(R.drawable.switch_tricks, null)
        binding.singUp.setTextColor(resources.getColor(R.color.titulos, null))
        binding.login.background = null
        binding.login.setTextColor(resources.getColor(R.color.botones, null))
        binding.layoutLogIn.visibility = View.GONE
        binding.layoutSingUp.visibility = View.VISIBLE
    }
    private fun registrarse(){
        val email = binding.eMailSingUp.text.toString()
        val password = binding.passwordSingUp.text.toString()
        val repePass = binding.repePassword.text.toString()

        //Verificar que los campos de email, password y repePass no esten vacios.
        if (email.isNotEmpty() && password.isNotEmpty() && repePass.isNotEmpty()){
            //Verificar que password y repePass sean iguales
            if (password == repePass){
                //Verificar que el correo no exista
                FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful){
                            val singInMethod = task.result?.signInMethods
                            if (singInMethod != null && singInMethod.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)){
                                usuarioExiste()
                            }else{
                                //Se comprueba que la longitud de la contraseña sea mayor a 6
                                if (password.length < 6){
                                    contrasenyaCorta()
                                }else {
                                    FirebaseAuth.getInstance()
                                        .createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                showHome(
                                                    task.result?.user?.email ?: "",
                                                    ProviderType.BASIC
                                                )
                                            } else {
                                                errorLogin()
                                            }
                                        }
                                }
                            }
                        }else{
                           errorRegistro()
                        }
                    }
            }else {
                contrasenyaDiferente()
            }
        }else{
            camposVacios()
        }
    }
    private fun iniciarSesion(){
        val email = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()

        //Verificar que los campos no esten vacios
        if (email.isNotEmpty() && password.isNotEmpty()){
            //Verificamos que exista el email
            FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
                .addOnCompleteListener{task ->
                    val singInMethod = task.result?.signInMethods
                    if (singInMethod != null && singInMethod.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)){
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener{ task ->
                                if(task.isSuccessful){
                                    showHome(task.result?.user?.email ?: "", ProviderType.BASIC)
                                }else{
                                    errorRegistro()
                                }
                            }
                    }else {
                        errorLogin()
                    }
                }
        }else{
            camposVacios()
        }
    }
    private fun showHome(email: String, provider: ProviderType){
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }
    private fun googleApi(){
        //Configuración
        val googleConf =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        val googleClient = GoogleSignIn.getClient(this, googleConf)
        googleClient.signOut()

        GOOGLE_SING_IN.launch(googleClient.signInIntent)
    }

    private fun session(){
        //Comprobamos que no haya ninguna sesion activa, si hay alguna, evitamos mostrar la pantalla de inicio de sesion
        //y mostramos directamente el menú de Incio.
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if(email != null && provider != null){
            binding.layoutPrincipal.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }
    }
    //Mensajes de Error
    private fun camposVacios(){
        Toast.makeText(this,
        R.string.camposVacios,
        Toast.LENGTH_SHORT).show()
    }
    private fun contrasenyaDiferente(){
        Toast.makeText(this,
            R.string.contrasenyaDiferente,
            Toast.LENGTH_SHORT).show()
    }
    private fun errorRegistro(){
        Toast.makeText(this,
            R.string.errorRegistro,
            Toast.LENGTH_SHORT).show()
    }
    private fun usuarioExiste(){
        Toast.makeText(this,
            R.string.usuarioExiste,
            Toast.LENGTH_SHORT).show()
    }
    private fun errorLogin(){
        Toast.makeText(this,
            R.string.errorLogin,
            Toast.LENGTH_SHORT).show()
    }
    private fun contrasenyaCorta(){
        Toast.makeText(this,
            R.string.contrasenyaCorta,
            Toast.LENGTH_SHORT).show()
    }
}