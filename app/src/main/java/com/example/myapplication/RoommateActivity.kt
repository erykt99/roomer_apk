package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RoommateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_roommate)

        val cloudDatabase = Firebase.firestore
        //tutaj robimy tak, losujemy liczbe z zakresu 0 do numberOfUsers -1
        // następnie szukamy usera o id ktory jest rowne wyszukanej liczby
        // i uzupelniamy wszystko tymi danymi

    }
}