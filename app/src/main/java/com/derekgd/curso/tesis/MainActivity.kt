package com.derekgd.curso.tesis

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.derekgd.curso.R
import com.derekgd.curso.tesis.clases.AppStatus
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val GOOGLE_SIGN_IN = 100
    val packageName1 = "com.derekgd.curso"
    lateinit var packageManager1: PackageManager
    private var isPasswordVisible:Boolean = false


    val instancia = AppStatus()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val sharedPrefer = getSharedPreferences("MyPrefers", Context.MODE_PRIVATE)
        val firstRun = sharedPrefer.getBoolean("isFirstRun",true)
        val theme = sharedPrefer.getString("appTheme","")
        packageManager1 = this.packageManager


        if(firstRun) {
            Log.e("primera","es la primera vez que entra")
            if (instancia.isAppInstalled(packageName1, packageManager1)) {
                Log.e("primera","es la primera vez que entra pero otro version de la app ya estaba instalada")
                auth.signOut()
                sharedPrefer.edit().putBoolean("isFirstRun",false).apply()

            }
        }

        when(theme){
            "Claro" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "Oscuro" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "Predeterminado" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            else -> {}
        }


        if (auth.currentUser != null) {
            val intent = Intent(this, BottomBarActivity::class.java)
            startActivity(intent)
        } else {

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

        val btnlogin = findViewById<Button>(R.id.login)
        val btnregister = findViewById<Button>(R.id.register)
        val loginGoogle = findViewById<Button>(R.id.loginGoogle)
        val tvEmail = findViewById<AppCompatEditText>(R.id.tvEmail)
        val tvPassword = findViewById<EditText>(R.id.tvPassword)
        val ibPasswordView = findViewById<ImageButton>(R.id.ibPasswordView)

        btnregister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        ibPasswordView.setOnClickListener {
            if (isPasswordVisible) {
                tvPassword.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                isPasswordVisible = false
                tvPassword.setSelection(tvPassword.text.length)
            }else{
                tvPassword.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                isPasswordVisible = true
                tvPassword.setSelection(tvPassword.text.length)
            }
        }


        btnlogin.setOnClickListener {
            val email: String = tvEmail.text.toString()
            val password: String = tvPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Campos Vacios", Toast.LENGTH_SHORT).show()
            } else {
                onSignInResult(email, password)
            }
        }

        loginGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }
    }


    private fun onSignInResult(Email: String, Password: String) {
        auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Inicio de sesion con exito", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, BottomBarActivity::class.java)
                startActivity(intent)
                this.finish()
            } else {
                Toast.makeText(
                    this,
                    "Se a producido un problema iniciando sesion",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.e("oogle55", "error no accede a la cuenta")
            try {
                val account = task.getResult(ApiException::class.java)
                Log.e("oogle55", "Si entra al try")
                if (account != null) {
                    Log.e("oogle55", "Cuenta nula")
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(this, BottomBarActivity::class.java)
                            startActivity(intent)
                            this.finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Lo sentimos hubo un error inesperado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "No selecciono una cuenta valida", Toast.LENGTH_SHORT).show()
            }
        }
    }

}