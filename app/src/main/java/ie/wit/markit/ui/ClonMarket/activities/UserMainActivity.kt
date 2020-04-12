package ie.wit.markit.ui.ClonMarket.activities

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity


class UserMainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ie.wit.R.layout.user_main_activity)
        val drawerLayout: DrawerLayout = findViewById(ie.wit.R.id.user_drawer_layout)
        val navView: NavigationView = findViewById(ie.wit.R.id.nav_view)
        val navController = findNavController(ie.wit.R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                ie.wit.R.id.nav_clon_trader, ie.wit.R.id.nav_clon_about_us, ie.wit.R.id.nav_Location
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(ie.wit.R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

