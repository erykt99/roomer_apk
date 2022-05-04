package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val cloudStorage = Firebase.firestore
        val auth = FirebaseAuth.getInstance();

        val username = findViewById<EditText>(R.id.Username)
        val age = findViewById<EditText>(R.id.userAge)
        val userInformation = findViewById<EditText>(R.id.userInfromation)
        val gender = findViewById<RadioGroup>(R.id.Gender)


        val emailView = findViewById<TextView>(R.id.textView3)
        emailView.text = auth.currentUser?.email ?: ""


        val saveButton = findViewById<Button>(R.id.btnSave)
        saveButton.setOnClickListener {
            if (userInformation.text.toString().isEmpty() || !gender.isPressed
                || age.text.toString().isEmpty() || username.text.toString().isEmpty()) {
                Toast.makeText(applicationContext,"Please fill all the information",Toast.LENGTH_LONG).show()
            } else {
                val radioButton = findViewById<RadioButton>(gender.checkedRadioButtonId)
                val user = hashMapOf(
                    "Name" to username.text.toString(),
                    "UserInformation" to userInformation.text.toString(),
                    "Age" to age.text.toString(),
                    "Gender" to radioButton.text.toString()
                )
                auth.currentUser?.let { it1 ->
                    cloudStorage.collection("users").document(it1.uid)
                        .set(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                }

            }
        }

        val settingsButtom = findViewById<ImageButton>(R.id.settingsButton)
        settingsButtom.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, settingsButtom)
            popupMenu.menuInflater.inflate(R.menu.profile_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.logout -> {
                        Firebase.auth.signOut()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
                true;
            })
            popupMenu.show()

        }
    }
}