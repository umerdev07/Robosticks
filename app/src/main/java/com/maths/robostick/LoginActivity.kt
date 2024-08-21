package com.maths.robostick

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
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

//        saveLoginState()

        binding.signUp.setOnClickListener {
            startActivity(Intent(applicationContext, SigninActivity::class.java))
            finish()
        }

        binding.forgottenPassword.setOnClickListener {
            val dialogFragment = ForgottenPassword()
            dialogFragment.show(supportFragmentManager, "ForgottenPasswordDialog")
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
                            startActivity(Intent(this , MainActivity::class.java))
                            finish()
                        } else {
                            try{
                                throw  task.exception ?: Exception("Unknown Error")
                            }catch (e :FirebaseAuthInvalidCredentialsException){
                                showAlertDialog("User not registered or incorrect username or password")
                            }catch (e :Exception){
                                showAlertDialog("Login Failed! Connect your network")
                            }
                        }
                    }
            }
        }
    }
    private fun saveLoginState() {
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            // User is logged in, navigate to MainActivity
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            // User is not logged in, stay on LoginActivity
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish() // Close the activity
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