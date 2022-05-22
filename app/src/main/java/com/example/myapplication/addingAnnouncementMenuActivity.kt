package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class addingAnnouncementMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_announcement_menu)


        val flat =  findViewById<Button>(R.id.lookingFlat)
        val roommate = findViewById<Button>(R.id.lookingRommate)


        flat.setOnClickListener {
            val intent = Intent(this, addingFlatActivity::class.java)
            startActivity(intent)

        }

        roommate.setOnClickListener {

            val intent = Intent(this, addingRommateActivity::class.java)
            startActivity(intent)
        }




    }
}