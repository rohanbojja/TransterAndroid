package com.rohanbojja.transter
// Activity to check for permissions and Auth. Bypass if already logged in. TODO
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_init.*

// Layout can be used as a splash screen

class InitActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 22

    private fun handleUserLogin(){
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // already signed in
            val intent  = Intent(this,MainActivity::class.java)
            startActivity(intent)
        } else {
            // not signed in
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build())

            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setLogo(R.drawable.ic_logo)
                    .setTheme(R.style.LoginTheme)
                    .build(),
                RC_SIGN_IN)
        }
    }

    private fun handleNewUserSignUp(){
        //Create a Firestore entry
        val db = Firebase.firestore
        val auth = FirebaseAuth.getInstance()
        val user = hashMapOf(
            "first" to "Your name"
        )
        db.collection("users").document(auth.currentUser!!.uid).set(user).addOnSuccessListener {
            println("User sign-up successful.")
            val intent  = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
        //Check for permissions TODO
        // Location FINE AND COARSE
        handleUserLogin()
        containedButton.setOnClickListener {
            handleUserLogin()
        }
        //Check if user is logged in
        //ENABLE SILENT SIGN IN SMART LOCK TODO

//        Security concerns TODO
//                Authentication using only a phone number, while convenient, is less secure than the other available methods, because possession of a phone number can be easily transferred between users. Also, on devices with multiple user profiles, any user that can receive SMS messages can sign in to an account using the device's phone number.
//
//        If you use phone number based sign-in in your app, you should offer it alongside more secure sign-in methods, and inform users of the security tradeoffs of using phone number sign-in.

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                println("Sign in SUCCESS")
                val user = FirebaseAuth.getInstance().currentUser
                val db = Firebase.firestore
                //TODO LOADER
                db.collection("users").document(user!!.uid).get()
                    .addOnSuccessListener {
                        if(it != null)
                        {
                            val intent  = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                        }
                        else
                        {
                            handleNewUserSignUp()
                        }
                    }
                    .addOnFailureListener {
                        println("Getting document failed!")
                    }

                // ...
            } else {
                println("Sign in failed")
            }
        }
    }

}
