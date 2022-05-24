package com.example.myapplication

import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.File


class addingFlatActivity : AppCompatActivity() {

    private val pickImage = 100
    private var imageUri: Uri? = null
    val storageReference = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adding_flat)
        val cloudStorage = Firebase.firestore

        val auth = FirebaseAuth.getInstance();
        val address = findViewById<TextView>(R.id.addAddress)
        val desc = findViewById<TextView>(R.id.roomDescription)
        val saveButton = findViewById<Button>(R.id.btnaddRoom)
        val imageButton = findViewById<Button>(R.id.addFlatImmage)



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


        imageButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }
    @Deprecated("Use the new activity method")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            val image = findViewById<ImageView>(R.id.imageView)
            image.setImageURI(imageUri)
            uploadUserImage()


        }else {
            Toast.makeText(this, "No added", Toast.LENGTH_SHORT).show()
        }


    }


    private fun uploadUserImage() {
        val FirebaseAuth = FirebaseAuth.getInstance();
        val fileName = FirebaseAuth.uid
        val storageReference = FirebaseStorage.getInstance().getReference("flats/$fileName")
        storageReference.putFile(imageUri!!).addOnSuccessListener {
            Toast.makeText(this, "Image added", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {
                Toast.makeText(this, "Can't add image", Toast.LENGTH_SHORT).show()
            }




//        val image = findViewById<ImageView>(R.id.imageView)
//
//        val storageReference2 = FirebaseStorage.getInstance().getReference().child("flats/$fileName")
//        val localFile = File.createTempFile("$fileName", "jpeg")
//        storageReference2.getFile(localFile)
//            .addOnSuccessListener {
//                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
//               image.setImageBitmap(bitmap)
//            }.addOnFailureListener {
//                Toast.makeText(this, "can't load image", Toast.LENGTH_SHORT).show()
//            }



    }


}