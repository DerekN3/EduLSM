package com.derekgd.curso.tesis

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.derekgd.curso.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class passwordActivity : AppCompatActivity() {

    private var isPasswordVisible: Boolean = false
    private lateinit var auth: FirebaseAuth
    val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvOldPassword = findViewById<EditText>(R.id.tvOldPassword)
        val tvNewPassword = findViewById<EditText>(R.id.tvNewPassword)
        val tvConfirmPassword = findViewById<EditText>(R.id.tvConfirmPassword)
        val ibOldPasswordView = findViewById<ImageButton>(R.id.ibOldPasswordView)
        val ibNewPasswordView = findViewById<ImageButton>(R.id.ibNewPasswordView)
        val ibConfirmPasswordView = findViewById<ImageButton>(R.id.ibConfirmPasswordView)
        val btnSaveAccount = findViewById<Button>(R.id.btnSaveAccount)
        auth = Firebase.auth

        ibOldPasswordView.setOnClickListener {
            if (isPasswordVisible) {
                tvOldPassword.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                isPasswordVisible = false
                tvOldPassword.setSelection(tvOldPassword.text.length)
            } else {
                tvOldPassword.inputType =
                    android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                isPasswordVisible = true
                tvOldPassword.setSelection(tvOldPassword.text.length)
            }
        }

        ibNewPasswordView.setOnClickListener {
            if (isPasswordVisible) {
                tvNewPassword.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                isPasswordVisible = false
                tvNewPassword.setSelection(tvNewPassword.text.length)
            } else {
                tvNewPassword.inputType =
                    android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                isPasswordVisible = true
                tvNewPassword.setSelection(tvNewPassword.text.length)
            }
        }

        ibConfirmPasswordView.setOnClickListener {
            if (isPasswordVisible) {
                tvConfirmPassword.inputType =
                    android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                isPasswordVisible = false
                tvConfirmPassword.setSelection(tvConfirmPassword.text.length)
            } else {
                tvConfirmPassword.inputType =
                    android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                isPasswordVisible = true
                tvConfirmPassword.setSelection(tvConfirmPassword.text.length)
            }
        }

        btnSaveAccount.setOnClickListener {
            if (tvNewPassword.text.toString() == tvConfirmPassword.text.toString()) {
                if (tvNewPassword.text.length >= 6) {
                    if (tvNewPassword.text.toString() != tvOldPassword.text.toString()) {
                        auth.signInWithEmailAndPassword(
                            user?.email.toString(),
                            tvOldPassword.text.toString()
                        )
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {

                                    user!!.updatePassword(tvNewPassword.text.toString())
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                Toast.makeText(
                                                    this,
                                                    "Contraseña cambiada con exito!",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                this.finish()
                                            }
                                        }

                                } else {
                                    Toast.makeText(
                                        this,
                                        "La contraseña actual que introdujo no es la correcta",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            this,
                            "La nueva contraseña no puede ser igual a la antigua",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "La contraseña nueva debe de tener mas de 6 caracteres",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "La nueva contraseña y la confirmacion deben ser iguales",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}