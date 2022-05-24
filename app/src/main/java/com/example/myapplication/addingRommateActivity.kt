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

class addingRommateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_rommate)

        val cloudStorage = Firebase.firestore
        val auth = FirebaseAuth.getInstance();

        val roommateDescription = findViewById<TextView>(R.id.roommateDescription)
        val roommateAddress =  findViewById<TextView>(R.id.rommateAddress)
        val btnAddRoommate =  findViewById<Button>(R.id.btnAddRoommate)

        btnAddRoommate.setOnClickListener {

            val desc  = roommateDescription.text.toString()
            val address =  roommateAddress.text.toString()

            if(desc.isBlank() || address.isBlank()){
                Toast.makeText(applicationContext,"Can't be blank", Toast.LENGTH_LONG).show()
            }else{
                val roommates = hashMapOf(
                    "ID" to Firebase.auth.uid,
                    "about" to desc,
                    "street" to address
                )
                auth.currentUser?.let { it1 ->
                    cloudStorage.collection("roommates").document(it1.uid)
                        .set(roommates)
                        .addOnSuccessListener { documentReference ->
                            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: ${documentReference}")
                            Toast.makeText(this, "Your announcement has been added", Toast.LENGTH_SHORT).show()

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