package com.rohanbojja.transter

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
        fab.setOnClickListener { view ->
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    println("Signed out.")
                    val intent  = Intent(this,InitActivity::class.java)
                    startActivity(intent)
                    finish()
                }
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
        }
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
                else -> false
            }
        }
        toggle.syncState()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.map, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send
            ), drawerLayout
        )
        val mapFragment: SupportMapFragment? = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


}
