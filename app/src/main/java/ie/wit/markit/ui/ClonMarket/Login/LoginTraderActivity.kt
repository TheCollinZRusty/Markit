package ie.wit.markit.ui.ClonMarket.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import ie.wit.AdminFragment.AdminActivity
import ie.wit.R
import ie.wit.markit.ui.ClonMarket.Admin.main.MainApp
import kotlinx.android.synthetic.main.activity_login.detail
import kotlinx.android.synthetic.main.activity_login.emailCreateAccountButton
import kotlinx.android.synthetic.main.activity_login.emailPasswordButtons
import kotlinx.android.synthetic.main.activity_login.emailPasswordFields
import kotlinx.android.synthetic.main.activity_login.emailSignInButton
import kotlinx.android.synthetic.main.activity_login.fieldEmail
import kotlinx.android.synthetic.main.activity_login.fieldPassword
import kotlinx.android.synthetic.main.activity_login.main_layout
import kotlinx.android.synthetic.main.activity_login.signOutButton
import kotlinx.android.synthetic.main.activity_login.signedInButtons
import kotlinx.android.synthetic.main.activity_login.status
import kotlinx.android.synthetic.main.activity_login.verifyEmailButton
import kotlinx.android.synthetic.main.login.*
import org.jetbrains.anko.startActivity


class LoginTraderActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var app: MainApp
    lateinit var loader : AlertDialog

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        app = application as MainApp
        // Buttons
        emailSignInButton.setOnClickListener(this)
        emailCreateAccountButton.setOnClickListener(this)
        signOutButton.setOnClickListener(this)
        verifyEmailButton.setOnClickListener(this)
        sign_in_button.setOnClickListener(this)


        app.auth = FirebaseAuth.getInstance()

        // [START config_signin]
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // [END config_signin]

        app.googleSignInClient = GoogleSignIn.getClient(this, gso)

        loader = ie.wit.AdminFragment.createLoader(this)

        sign_in_button.setSize(SignInButton.SIZE_WIDE)
        sign_in_button.setColorScheme(0)


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = app.auth.currentUser
        updateUI(currentUser)
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        ie.wit.AdminFragment.showLoader(loader, "Creating Account...")
        // [START create_user_with_email]
        app.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = app.auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
                // [START_EXCLUDE]
                ie.wit.AdminFragment.hideLoader(loader)
                // [END_EXCLUDE]
            }
        // [END create_user_with_email]
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }
        ie.wit.AdminFragment.showLoader(loader, "Logging In...")
        // [START sign_in_with_email]
        app.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = app.auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
                // [START_EXCLUDE]
                if (!task.isSuccessful) {
                    status.setText(R.string.auth_failed)
                }
                ie.wit.AdminFragment.hideLoader(loader)
                // [END_EXCLUDE]
            }
        // [END sign_in_with_email]
    }

    private fun signOut() {
        app.auth.signOut()
        app.googleSignInClient.signOut()
        updateUI(null)
    }

    private fun sendEmailVerification() {
        // Disable button
        verifyEmailButton.isEnabled = false

        // Send verification email
        // [START send_email_verification]
        val user = app.auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                // [START_EXCLUDE]
                // Re-enable button
                verifyEmailButton.isEnabled = true

                if (task.isSuccessful) {
                    Toast.makeText(baseContext,
                        "Verification email sent to ${user.email} ",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(baseContext,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
                // [END_EXCLUDE]
            }
        // [END send_email_verification]
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail.error = "Required."
            valid = false
        } else {
            fieldEmail.error = null
        }

        val password = fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            fieldPassword.error = "Required."
            valid = false
        } else {
            fieldPassword.error = null
        }

        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        ie.wit.AdminFragment.hideLoader(loader)
        if (user != null) {
            status.text = getString(R.string.emailpassword_status_fmt,
                user.email, user.isEmailVerified)
            detail.text = getString(R.string.firebase_status_fmt, user.uid)

            emailPasswordButtons.visibility = View.GONE
            emailPasswordFields.visibility = View.GONE
            signedInButtons.visibility = View.VISIBLE

            verifyEmailButton.isEnabled = !user.isEmailVerified
            app.database = FirebaseDatabase.getInstance().reference
            app.storage = FirebaseStorage.getInstance().reference

            startActivity<AdminActivity>()
        } else {
            status.setText(R.string.signed_out)
            detail.text = null

            emailPasswordButtons.visibility = View.VISIBLE
            emailPasswordFields.visibility = View.VISIBLE
            signedInButtons.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.emailCreateAccountButton -> createAccount(fieldEmail.text.toString(), fieldPassword.text.toString())
            R.id.emailSignInButton -> signIn(fieldEmail.text.toString(), fieldPassword.text.toString())
            R.id.signOutButton -> signOut()
            R.id.verifyEmailButton -> sendEmailVerification()
            R.id.sign_in_button -> googleSignIn()
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
        private const val RC_SIGN_IN = 9001
    }


    // [START onactivityresult]
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START google signin]
    private fun googleSignIn() {
        val signInIntent = app.googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END google signin]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)
        // [START_EXCLUDE silent]
        ie.wit.AdminFragment.showLoader(loader, "Logging In with Google...")
        // [END_EXCLUDE]

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        app.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = app.auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Snackbar.make(main_layout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // [START_EXCLUDE]
                ie.wit.AdminFragment.hideLoader(loader)
                // [END_EXCLUDE]
            }
    }
    // [END auth_with_google]
}