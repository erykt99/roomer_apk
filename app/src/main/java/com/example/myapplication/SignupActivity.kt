package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignupActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    val cloudStorage = Firebase.firestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val btnBackToLogin = findViewById<Button>(R.id.btnBackToLogin)
        btnBackToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val email = findViewById<EditText>(R.id.newEmail)
        //val username = findViewById<EditText>(R.id.newUsername)
        val password = findViewById<EditText>(R.id.newPassword)

        auth = FirebaseAuth.getInstance()

        val btnSignUp = findViewById<Button>(R.id.btnMakeAcc)
        btnSignUp.setOnClickListener {
            val newemail = email.text.toString()
            val newpassword = password.text.toString()

            if (newemail.isBlank() || newpassword.isBlank()) {
                Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(newemail, newpassword)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Successfully Singed Up", Toast.LENGTH_SHORT)
                                .show()
                            val intent2 = Intent(this, ProfileActivity::class.java)
                            val currentUser = auth.currentUser
                            if (currentUser != null) {
                                intent2.putExtra("User",currentUser.email)
                            }
                            val user = hashMapOf(
                                "Name" to "Username",
                                "UserInformation" to "Tell something about you",
                                "Age" to "Age",
                                "Gender" to "Gender",
                                "ID" to Firebase.auth.uid
                            )
                            auth.currentUser?.let { it1 ->
                                cloudStorage.collection("users").document(it1.uid)
                                    .set(user)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference}")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(ContentValues.TAG, "Error adding document", e)
                                    }
                            }
                            startActivity(intent2)
                        } else {
                            Toast.makeText(this, "Singed Up Failed!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}
