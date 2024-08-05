package com.derekgd.curso.tesis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.derekgd.curso.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private var isPasswordVisible:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = Firebase.auth

        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email = findViewById<AppCompatEditText>(R.id.rEmail)
        val btnregister = findViewById<Button>(R.id.rRegister)
        val password = findViewById<EditText>(R.id.rPassword)
        val ibPasswordView = findViewById<ImageButton>(R.id.ibPasswordView)

        ibPasswordView.setOnClickListener {
            if (isPasswordVisible) {
                password.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                isPasswordVisible = false
                password.setSelection(password.text.length)
            }else{
                password.inputType = android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                isPasswordVisible = true
                password.setSelection(password.text.length)
            }
        }


        btnregister.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            val txt_email: String = email.text.toString()
            val txt_password: String = password.text.toString()

            if (txt_email.isEmpty() || txt_password.isEmpty()) {
                Toast.makeText(this, "Campos Vacios", Toast.LENGTH_SHORT).show()
            } else if (txt_password.length < 6) {
                Toast.makeText(this, "Contraseña demasiado corta", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(txt_email,txt_password)
            }

        }


    }

    private fun registerUser(Email: String, Password: String) {
        auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                auth.signOut()
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                this.finish()
            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(this, "No se pudo registrar", Toast.LENGTH_SHORT).show()

            }
        }

    }


}