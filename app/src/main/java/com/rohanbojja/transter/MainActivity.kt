package com.rohanbojja.transter

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.firebase.ui.auth.AuthUI
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.OnMapReadyCallback
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MapStyleOptions
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity(),OnMapReadyCallback{

    private lateinit var appBarConfiguration: AppBarConfiguration
    //TODO Secure Maps API key

    override fun onMapReady(p0: GoogleMap?) {
        val googleMap = p0
        println("Map is ready.")
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success: Boolean = googleMap!!.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.style_json
                )
            )
            if (!success) {
            }
        } catch (e: Resources.NotFoundException) {
        }
        val sydney = LatLng(-34.0, 151.0)
        googleMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val user = FirebaseAuth.getInstance().currentUser
        //Inflate views
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayUseLogoEnabled(true)
        supportActionBar!!.setLogo(R.drawable.ic_logo_small)
        val fab: ExtendedFloatingActionButton = findViewById(R.id.fab)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        val navigationView: NavigationView = findViewById(R.id.navigation)
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.map -> {
                    println("MAP SELECTED.")
                    true
                }
                R.id.nav_gallery -> {
                    val intent  = Intent(this,InitActivity::class.java);startActivity(intent)
                    finish()
                    true
                }
                R.id.logout -> {
                    AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener {
                            println("Signed out.")
                            val intent  = Intent(this,InitActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    true
                }
                else -> false
            }
        }
        toggle.syncState()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val mapFragment: SupportMapFragment? = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        //Setup navigation pane
        val navigationHeader = navigationView.getHeaderView(0)
        val nameHeader = navigationHeader.findViewById<TextView>(R.id.textView2)
        val emailHeader = navigationHeader.findViewById<TextView>(R.id.textView)
        val phoneHeader = navigationHeader.findViewById<TextView>(R.id.textView3)
        val setupSocialButton = navigationHeader.findViewById<Button>(R.id.setupSocial)
        setupSocialButton.visibility = View.GONE
        setupSocialButton.setOnClickListener {
            val intent  = Intent(this,SocialSetup::class.java)
            startActivity(intent)
            finish()
        }
        nameHeader.text = user!!.uid
        if(user.email == null)
        {
            setupSocialButton.visibility = View.VISIBLE
            emailHeader.visibility = View.GONE
        }
        else{
            emailHeader.text = user.email
        }

        if(user.displayName == null){
            setupSocialButton.visibility = View.VISIBLE
            nameHeader.visibility = View.GONE
        }
        else{
            nameHeader.text = user.displayName
        }
        if(user.phoneNumber == null){
            setupSocialButton.visibility = View.VISIBLE
            phoneHeader.visibility = View.GONE
        }
        else{
            phoneHeader.text = user.phoneNumber
        }


        //On click listener for FAB
        fab.setOnClickListener { view ->

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


}
