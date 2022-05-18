package com.example.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private val pickImage = 100
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val cloudStorage = Firebase.firestore
        val auth = FirebaseAuth.getInstance();
        val StorageReference = Firebase.storage.reference


        val username = findViewById<EditText>(R.id.Username)
        val age = findViewById<EditText>(R.id.userAge)
        val userInformation = findViewById<EditText>(R.id.userInfromation)
        val gender = findViewById<RadioGroup>(R.id.Gender)
        val emailView = findViewById<TextView>(R.id.textView3)
        val chooseImage = findViewById<ImageButton>(R.id.addImageButton)
        val noneButton = findViewById<RadioButton>(R.id.noneButton)
        val menuButton = findViewById<Button>(R.id.menuButton)
        val userImage = findViewById<ImageView>(R.id.userImage)


        chooseImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)

        }
        emailView.text = auth.currentUser?.email ?: ""

        val ID = Firebase.auth.uid.toString()

        val docRef = cloudStorage.collection("users").document(ID)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    val hint = document.get("Name").toString()
                    val age2 = document.get("Age").toString()
                    val UF  = document.get("UserInformation").toString()
                    gender.check(noneButton.id)
                    userInformation.hint = UF
                    userAge.hint = age2
                    username.hint = hint;

                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }


        val saveButton = findViewById<Button>(R.id.btnSave)



        saveButton.setOnClickListener {

            val userinfo2 = userInformation.text.toString()
            val userage2 = age.text.toString()
            val username2 = username.text.toString()
            val radioButton = findViewById<RadioButton>(gender.checkedRadioButtonId)
            val radiobutton2 = radioButton.text.toString()

           if (userinfo2.isBlank() || userage2.isBlank() || username2.isBlank()) {
                Toast.makeText(applicationContext,"Please fill all the information",Toast.LENGTH_LONG).show()
           } else {


    //            val radioButton = findViewById<RadioButton>(gender.checkedRadioButtonId)
                val user = hashMapOf(
                    "Name" to username2,
                    "UserInformation" to userinfo2,
                    "Age" to userage2,
                    "Gender" to radiobutton2,
                    "ID" to Firebase.auth.uid
                )
                auth.currentUser?.let { it1 ->
                    cloudStorage.collection("users").document(it1.uid)
                        .set(user)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference}")
                            Toast.makeText(this, "Data added", Toast.LENGTH_SHORT).show()

                            val intent = Intent(this, menuActivity::class.java)
                            startActivity(intent)

                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                }
               val intent = Intent(this, menuActivity::class.java)
               startActivity(intent)
            }

        }


        menuButton.setOnClickListener{
            val intent = Intent(this, menuActivity::class.java)
            startActivity(intent)
        }

        val settingsButton = findViewById<ImageButton>(R.id.settingsButton)
        settingsButton.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, settingsButton)
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            userImage.setImageURI(imageUri)


        }
    }



}