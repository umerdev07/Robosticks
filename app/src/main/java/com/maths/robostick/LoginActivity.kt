package com.maths.robostick

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.maths.robostick.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.signUp.setOnClickListener {
            startActivity(Intent(applicationContext, SigninActivity::class.java))
            finish()
        }

        binding.login.setOnClickListener {
            val emailUser = binding.loginEmailUsername.text.toString()
            val loginPassword = binding.loginPassword.text.toString()

            if (emailUser.isEmpty() || loginPassword.isEmpty()) {
                showAlertDialog("Both fields must be filled")
            } else {
                auth.signInWithEmailAndPassword(emailUser, loginPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java).apply {
                                putExtra("email", emailUser)
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            showAlertDialog("Failed to login. You may not be registered.")
                        }
                    }
            }
        }
    }
    private fun showAlertDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setTitle("Login")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}