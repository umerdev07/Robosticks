
package com.maths.robostick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.maths.robostick.databinding.ActivityPhoneBinding
import java.util.concurrent.TimeUnit

class PhoneActivity : DialogFragment() {
    private val binding: ActivityPhoneBinding by lazy {
        ActivityPhoneBinding.inflate(layoutInflater)
    }
    private lateinit var verificationId: String
    private var onGameResetListener: OnGameResetListener? = null

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

        val phoneNumber = arguments?.getString("formattedPhoneNumber")
        if (phoneNumber != null) {
            binding.Phone.setText(phoneNumber)
            startPhoneNumberVerification(phoneNumber)
        } else {
            Toast.makeText(requireContext(), "Phone number is missing", Toast.LENGTH_SHORT).show()
        }

        binding.verify.setOnClickListener {
            val code = binding.OtpTextView.text.toString()
            if (code.isNotEmpty()) {
                verifyPhoneNumberWithCode(verificationId, code)
            } else {
                Toast.makeText(requireContext(), "Please Enter Verification code", Toast.LENGTH_SHORT).show()
            }
        }
        binding.close.setOnClickListener {
            dismiss()
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(requireContext(), "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(id, token)
                verificationId = id
            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            requireActivity(),
            callback
        )
    }

    private fun verifyPhoneNumberWithCode(verificationId: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    onGameResetListener?.onGameReset()
                    dismiss()  // Close the dialog
                    Toast.makeText(requireContext(), "Verification Successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Verification Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun setOnGameResetListener(listener: OnGameResetListener) {
        onGameResetListener = listener
    }

    interface OnGameResetListener {
        fun onGameReset()
    }
    companion object {
        fun newInstance(formattedPhoneNumber: String): PhoneActivity {
            return PhoneActivity().apply {
                arguments = Bundle().apply {
                    putString("formattedPhoneNumber", formattedPhoneNumber)
                }
            }
        }
    }
}
