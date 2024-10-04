    package com.maths.robostick.UserManagment

    import android.Manifest
    import android.app.AlertDialog
    import android.content.Intent
    import android.content.pm.PackageManager
    import android.net.Uri
    import android.os.Bundle
    import android.util.Log
    import android.view.WindowManager
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.app.ActivityCompat
    import androidx.core.content.ContextCompat
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.database.DatabaseReference
    import com.google.firebase.database.FirebaseDatabase
    import com.google.firebase.storage.FirebaseStorage
    import com.google.i18n.phonenumbers.NumberParseException
    import com.google.i18n.phonenumbers.PhoneNumberUtil
    import com.maths.robostick.DataClasses.StudentCredentials
    import com.maths.robostick.databinding.ActivitySigninBinding

    @Suppress("DEPRECATION")
    class SigninActivity : AppCompatActivity() {
        private val binding: ActivitySigninBinding by lazy {
            ActivitySigninBinding.inflate(layoutInflater)
        }
        private lateinit var auth: FirebaseAuth
        private lateinit var databaseReference: DatabaseReference
        private lateinit var storage: FirebaseStorage
        private var selectedImg: Uri? = null
        private lateinit var progressdialog: AlertDialog.Builder

        private val MEDIA_PERMISSION_CODE = 123
        private val STORAGE_PERMISSION_CODE = 113
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            supportActionBar?.hide()
            setContentView(binding.root)

            auth = FirebaseAuth.getInstance()
            databaseReference = FirebaseDatabase.getInstance().reference
            storage = FirebaseStorage.getInstance()

            progressdialog = AlertDialog.Builder(this)
                .setTitle("Updating Profile...")
                .setCancelable(false)

            binding.circleProfileImageView.setOnClickListener {
                if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, MEDIA_PERMISSION_CODE)) {
                    openMediaPicker()
                }
            }

            binding.logIn.setOnClickListener {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }
            binding.signUp.setOnClickListener {
                val phoneNumber = binding.signinPhoneNumber.text.toString()
                val name = binding.name.text.toString()
                val email = binding.signinEmailUsername.text.toString()
                val password = binding.signinPassword.text.toString()
                val schoolName = binding.schoolName.text.toString()
                val grade = binding.grades.text.toString()

                // Check if any field is empty
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || schoolName.isEmpty() || grade.isEmpty()) {
                    showAlertDialog("Fill all Credential")
                } else if (selectedImg == null) {
                    Toast.makeText(applicationContext, "Please select your profile image", Toast.LENGTH_SHORT).show()
                } else {
                    checkIfEmailExists(email) { exists ->
                        if (exists) {
                            showAlertDialog("Email already exists. Please log in or use another email.")
                        } else {
                            // Proceed with phone number validation and user creation
                            if (phoneNumber.isNotEmpty()) {
                                val formattedNumber = formatPhoneNumber(phoneNumber)
                                if (formattedNumber != null) {
                                    // Create a new instance of PhoneActivity with formatted phone number
                                    val phoneDialog = PhoneActivity.newInstance(formattedNumber).apply {
                                        setOnGameResetListener(object :
                                            PhoneActivity.OnGameResetListener {
                                            override fun onGameReset() {
                                                uploadInfo() // Call uploadInfo() on successful phone verification
                                            }
                                        })
                                    }
                                    // Show the dialog
                                    phoneDialog.show(supportFragmentManager, "PhoneDialog")
                                } else {
                                    Toast.makeText(this, "Invalid phone number format", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

        }
        private fun formatPhoneNumber(phoneNumber: String): String? {
            val phoneNumberUtil = PhoneNumberUtil.getInstance()
            return try {
                val number = phoneNumberUtil.parse(phoneNumber, "PK")
                phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164)
            } catch (e: NumberParseException) {
                null
            }
        }

        private fun uploadInfo() {
            val name = binding.name.text.toString()
            val email = binding.signinEmailUsername.text.toString()
            val password = binding.signinPassword.text.toString()
            val fatherName = binding.fatherName.text.toString()
            val phoneNumber = binding.signinPhoneNumber.text.toString()
            val schoolName = binding.schoolName.text.toString()
            val grade = binding.grades.text.toString()
            val address = binding.address.text.toString()



            progressdialog.setMessage("Save Data...")
            val dialog = progressdialog.create()
            dialog.show()
            val reference = storage.reference.child("Profile").child(System.currentTimeMillis().toString())
            selectedImg?.let { imgUri ->
                reference.putFile(imgUri).addOnCompleteListener { uploadTask ->
                    if (uploadTask.isSuccessful) {
                        reference.downloadUrl.addOnSuccessListener { downloadUri ->
                            val imgUrl = downloadUri.toString()
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        storeAdditionalInformation(name, email, password, fatherName, phoneNumber, schoolName, grade, address, imgUrl)
                                        Toast.makeText(applicationContext, "Sign up Successfully", Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                                        finish()
                                    } else {
                                        showAlertDialog("Failed to Signup: ${task.exception?.message}")
                                        dialog.dismiss()
                                    }
                                }
                        }
                    } else {
                        showAlertDialog("Failed to upload image: ${uploadTask.exception?.message}")
                    }
                }
            }
        }

        private fun storeAdditionalInformation(
            name: String,
            email: String,
            password: String,
            fatherName: String,
            phoneNumber: String,
            schoolName: String,
            grade: String,
            address: String,
            imgUrl: String
        ) {
            val currentUser = auth.currentUser
            currentUser?.let { user ->
                val studentKey = databaseReference.child("users").child(user.uid).child("Students Data").push().key
                val studentsData = StudentCredentials(name, email, password, fatherName, phoneNumber, schoolName, grade, address, imgUrl)

                if (studentKey != null) {
                    databaseReference.child("users").child(user.uid).child("Students Data")
                        .child(studentKey).setValue(studentsData)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(applicationContext, "Data Saved Successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(applicationContext, "Failed to Save Data", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Log.e("SigninActivity", "Failed to generate student key")
                    Toast.makeText(applicationContext, "Failed to generate student key", Toast.LENGTH_SHORT).show()
                }
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == 1 && resultCode == RESULT_OK) {
                data?.data?.let { uri ->
                    selectedImg = uri
                    binding.circleProfileImageView.setImageURI(selectedImg)

                }
            }
        }

        private fun showAlertDialog(message: String) {
            AlertDialog.Builder(this)
                .setMessage(message)
                .setTitle("Sign Up")
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

        private fun checkPermission(permission: String, requestCode: Int): Boolean {
            return if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
                // Request permission
                ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
                false
            } else {
                // Permission is already granted
                true
            }
        }

        private fun openMediaPicker() {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*" // Set MIME type for images
            }
            startActivityForResult(intent, 1)
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == MEDIA_PERMISSION_CODE || requestCode == STORAGE_PERMISSION_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, now you can proceed
                    if (requestCode == MEDIA_PERMISSION_CODE) {
                        openMediaPicker()
                    }
                } else {
                    Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun checkIfEmailExists(email: String, callback: (exists: Boolean) -> Unit) {
            val auth = FirebaseAuth.getInstance()
            auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val signInMethods = task.result?.signInMethods
                        callback(!signInMethods.isNullOrEmpty())
                    } else {
                        // Handle the error
                        Toast.makeText(this, "Error checking email existence", Toast.LENGTH_SHORT).show()
                        callback(false)
                    }
                }
        }


    }
