package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class addingFlatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_flat)
        val cloudStorage = Firebase.firestore

        val auth = FirebaseAuth.getInstance();
        val address = findViewById<TextView>(R.id.addAddress)
        val desc = findViewById<TextView>(R.id.roomDescription)
        val saveButton = findViewById<Button>(R.id.btnaddRoom)


        saveButton.setOnClickListener {

            val address2 = address.text.toString()
            val desc2 =  desc.text.toString()

            if(address2.isBlank() || desc2.isBlank()){
                Toast.makeText(applicationContext,"Can't be blank", Toast.LENGTH_LONG).show()
            }else{
                val announcement = hashMapOf(
                    "street" to address2,
                    "description" to desc2,
                    "ID" to Firebase.auth.uid
                )
                auth.currentUser?.let { it1 ->
                    cloudStorage.collection("flat").document(it1.uid)
                        .set(announcement)
                        .addOnSuccessListener { documentReference ->
                            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference}")
                            Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, menuActivity::class.java)
                            startActivity(intent)

                        }
                        .addOnFailureListener { e ->
                            Log.w(ContentValues.TAG, "Error adding document", e)
                        }
                }


            }

        }


    }
}