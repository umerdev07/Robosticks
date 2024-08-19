package com.maths.robostick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.maths.robostick.databinding.ActivityForgottenPasswordBinding

class ForgottenPassword : DialogFragment() {
    private val binding: ActivityForgottenPasswordBinding by lazy {
        ActivityForgottenPasswordBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.close.setOnClickListener {
            dismiss()
        }

        binding.Reset.setOnClickListener {
            val password = binding.Emails.text.toString()
            auth.sendPasswordResetEmail(password)
                .addOnSuccessListener {
                    Toast.makeText(context, "Please check Email", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }
    }
}