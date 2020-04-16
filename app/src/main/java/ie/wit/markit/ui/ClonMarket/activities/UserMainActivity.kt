package ie.wit.markit.ui.ClonMarket.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import ie.wit.AdminFragment.*
import ie.wit.R
import ie.wit.markit.ui.ClonMarket.Admin.main.MainApp
import ie.wit.markit.ui.ClonMarket.ui_user.ClonAboutUs.ClonLocation
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


        nav_view.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, user_drawer_layout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        user_drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.getHeaderView(0).nav_header_email.text = app.auth.currentUser?.email

        //Checking if Google User, upload google profile pic
        checkExistingPhoto(app, this)

        nav_view.getHeaderView(0).imageView
            .setOnClickListener { showImagePicker(this, 1) }

        ft = supportFragmentManager.beginTransaction()

        val fragment = ClonPostsFragment.newInstance()
        ft.replace(R.id.homeFrame, fragment)
        ft.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_clon_about_us ->
                navigateTo(ClonLocation.newInstance())
            R.id.nav_clon_trader ->
                navigateTo(ClonTraderFragment.newInstance())
            R.id.nav_Location ->
                navigateTo(ClonLocationFragment.newInstance())
            R.id.nav_Trader_Posts ->
                navigateTo(ClonPostsFragment.newInstance())
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
        app.googleSignInClient.signOut().addOnCompleteListener(this) {
            app.auth.signOut()
            startActivity<LoginTraderActivity>()
            finish()
        }
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
                        .into(navView.getHeaderView(0).imageView, object : Callback {
                            override fun onSuccess() {
                                // Drawable is ready
                                uploadImageView(app, navView.getHeaderView(0).imageView
                                )
                            }
                            override fun onError(e: Exception) {}
                        })
                }
            }
        }
    }
}
