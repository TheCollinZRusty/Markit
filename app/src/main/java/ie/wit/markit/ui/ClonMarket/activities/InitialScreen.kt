package ie.wit.markit.ui.ClonMarket.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ie.wit.AdminFragment.Login
import ie.wit.R
import kotlinx.android.synthetic.main.activity_initial_screen.*

class InitialScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_screen)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        JumploginButton.setOnClickListener() {
            val intent = Intent(this, UserMainActivity::class.java)
            startActivity(intent)
        }
        JumpSignUpButton.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}