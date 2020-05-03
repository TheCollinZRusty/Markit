package ie.wit.markit.ui.ClonMarket.activities

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import ie.wit.AdminFragment.*
import ie.wit.R
import ie.wit.fragments.FavouritesFragment
import ie.wit.markit.ui.ClonMarket.Admin.helpers.checkLocationPermissions
import ie.wit.markit.ui.ClonMarket.Admin.helpers.isPermissionGranted
import ie.wit.markit.ui.ClonMarket.Admin.helpers.setCurrentLocation
import ie.wit.markit.ui.ClonMarket.Admin.main.MainApp
import ie.wit.markit.ui.ClonMarket.ui_user.ClonAboutUs.ClonAboutUsFragment
import ie.wit.markit.ui.ClonMarket.ui_user.ClonLocation.ClonLocationFragment
import ie.wit.markit.ui.ClonMarket.ui_user.ClonPosts.ClonPostsFragment
import ie.wit.markit.ui.ClonMarket.ui_user.ClonTrader.ClonTraderFragment
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.admin_activity.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.nav_header_admin.view.*
import kotlinx.android.synthetic.main.user_main_activity.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class UserMainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    lateinit var ft: FragmentTransaction
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_main_activity)
//        setSupportActionBar(toolbar)
        app = application as MainApp

        app.locationClient = LocationServices.getFusedLocationProviderClient(this)

        if(checkLocationPermissions(this)) {
            // todo get the current location
            setCurrentLocation(app)
        }
        nav_view.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, user_drawer_layout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        user_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        if(app.currentUser.email != null)
            nav_view.getHeaderView(0).nav_header_email.text = app.currentUser.email
        else
            nav_view.getHeaderView(0).nav_header_email.text = "No Email Specified..."

        //Checking if Google User, upload google profile pic
        checkExistingPhoto(app, this)

        nav_view.getHeaderView(0).imageView
            .setOnClickListener { showImagePicker(this, 1) }

        ft = supportFragmentManager.beginTransaction()

        val fragment = ClonAboutUsFragment.newInstance()
        ft.replace(R.id.homeFrame, fragment)
        ft.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_clon_about_us ->
                navigateTo(ClonAboutUsFragment.newInstance())
            R.id.nav_clon_trader ->
                navigateTo(ClonTraderFragment.newInstance())
            R.id.nav_Location ->
                navigateTo(ClonLocationFragment.newInstance())
            R.id.nav_Trader_Posts ->
                navigateTo(ClonPostsFragment.newInstance())
            R.id.nav_favourites ->
                navigateTo(FavouritesFragment.newInstance())
            R.id.nav_sign_out -> signOut()

            else -> toast("You Selected Something Else")
        }
        user_drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (user_drawer_layout.isDrawerOpen(GravityCompat.START))
            user_drawer_layout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener { startActivity<InitialScreen>() }
        finish()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (data != null) {
                    writeImageRef(app, readImageUri(resultCode, data).toString())
                    Picasso.get().load(readImageUri(resultCode, data).toString())
                        .resize(180, 180)
                        .transform(CropCircleTransformation())
                        .into(nav_view.getHeaderView(0).imageView, object : Callback {
                            override fun onSuccess() {
                                // Drawable is ready
                                uploadImageView(app, nav_view.getHeaderView(0).imageView
                                )
                            }
                            override fun onError(e: Exception) {}
                        })
                }
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (isPermissionGranted(requestCode, grantResults)) {
            // todo get the current location
            setCurrentLocation(app)
        } else {
            // permissions denied, so use a default location
            app.currentLocation = Location("Default").apply {
                latitude = 52.245696
                longitude = -7.139102
            }
        }
        Log.v("Trader", "Home LAT: ${app.currentLocation.latitude} LNG: ${app.currentLocation.longitude}")
    }
}
