package com.rohanbojja.transter

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.firebase.ui.auth.AuthUI
import com.google.android.apps.gmm.map.util.jni.NativeHelper.context
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
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),OnMapReadyCallback, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener<Any>{

    private lateinit var appBarConfiguration: AppBarConfiguration
    //TODO Secure Maps API key
    private lateinit var rfaBtn: RapidFloatingActionButton
    private lateinit var rfaLayout: RapidFloatingActionLayout
    private lateinit var rfabHelper: RapidFloatingActionHelper
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

    override fun onRFACItemIconClick(position: Int, item: RFACLabelItem<Any>?) {
        println("${position} ${item.toString()}")
        rfabHelper.toggleContent();
    }

    override fun onRFACItemLabelClick(position: Int, item: RFACLabelItem<Any>?) {
        println("${position} ${item.toString()}")
        rfabHelper.toggleContent()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Fill the expanding FAB
        rfaLayout = findViewById<RapidFloatingActionLayout>(R.id.activity_main_rfal)
        rfaBtn = findViewById<RapidFloatingActionButton>(R.id.activity_main_rfab)
        val rfaContent = RapidFloatingActionContentLabelList(this)
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this)
        val items = mutableListOf<RFACLabelItem<Int>>(
            RFACLabelItem<Int>()
                .setLabel("Mechanic")
                .setResId(R.drawable.ic_mech)
                .setIconNormalColor(-0x27bceb)
                .setIconPressedColor(-0x40c9f4)
                .setWrapper(0),

            RFACLabelItem<Int>()
                .setLabel("Flat tyre")
                .setResId(R.drawable.ic_trip_origin)
                .setIconNormalColor(-0x27bceb)
                .setIconPressedColor(-0x40c9f4)
                .setWrapper(0),

            RFACLabelItem<Int>()
                .setLabel("Fuel")
                .setResId(R.drawable.ic_local_gas_station)
                .setIconNormalColor(-0x27bceb)
                .setIconPressedColor(-0x40c9f4)
                .setWrapper(0),

            RFACLabelItem<Int>()
                .setLabel("Key")
                .setResId(R.drawable.ic_key)
                .setIconNormalColor(-0x27bceb)
                .setIconPressedColor(-0x40c9f4)
                .setWrapper(0),

            RFACLabelItem<Int>()
                .setLabel("Car wash")
                .setResId(R.drawable.ic_local_car_wash)
                .setIconNormalColor(-0x27bceb)
                .setIconPressedColor(-0x40c9f4)
                .setWrapper(0),

            RFACLabelItem<Int>()
                .setLabel("Towing")
                .setResId(R.drawable.ic_local_shipping)
                .setIconNormalColor(-0x27bceb)
                .setIconPressedColor(-0x40c9f4)
                .setWrapper(0)
        ) as List<RFACLabelItem<Int>>
        rfaContent
            .setItems(items)
            .setIconShadowColor(-0x777778)
        rfabHelper = RapidFloatingActionHelper(
            context,
            rfaLayout,
            rfaBtn,
            rfaContent
        ).build()


        val user = FirebaseAuth.getInstance().currentUser

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.navigation)
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.map -> {
                    println("MAP SELECTED.")
                    true
                }
                R.id.edit_profile -> {
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

        //On-click listener for FAB hamburger
        fabHamburger.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}
