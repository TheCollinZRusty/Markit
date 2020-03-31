package ie.wit.AdminFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.wit.R
import ie.wit.markit.ui.ClonMarket.Admin.main.MainApp
import kotlinx.android.synthetic.main.fragment_add_trader.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.HashMap


class AddTraderFragment : Fragment(), AnkoLogger {

    lateinit var app: MainApp
    var totalDonated = 0
    lateinit var loader: AlertDialog
    lateinit var eventListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_add_trader, container, false)
        loader = createLoader(activity!!)
        activity?.title = getString(R.string.action_add_trader)


        setButtonListener(root)
        return root;
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddTraderFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    fun setButtonListener(layout: View) {
        layout.AddButton.setOnClickListener {
            val title = layout.Name.text.toString()
            val description = layout.Description.text.toString()
            val number = layout.Number.text.toString()
            writeNewTrader(
                ClonTraderModel(
                    Name = title, Description = description, Number = number,
                    profilepic = app.userImage.toString(),
                    email = app.auth.currentUser?.email
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        tradertotal(app.auth.currentUser?.uid)
    }

    override fun onPause() {
        super.onPause()
        if (app.auth.uid != null)
            app.database.child("user-traders")
                .child(app.auth.currentUser!!.uid)
                .removeEventListener(eventListener)
    }

    fun writeNewTrader(clonTrader: ClonTraderModel) {
//         Create new clonTrader at /clonTraders & /clonTraders/$uid
        showLoader(loader, "Adding Donation to Firebase")
        info("Firebase DB Reference : $app.database")
        val uid = app.auth.currentUser!!.uid
        val key = app.database.child("traders").push().key
        if (key == null) {
            info("Firebase Error : Key Empty")
            return
        }
        clonTrader.uid = key
        val traderValues = clonTrader.toMap()
        val childUpdates = HashMap<String, Any>()
        childUpdates["/traders/$key"] = traderValues
        childUpdates["/user-traders/$uid/$key"] = traderValues
        app.database.updateChildren(childUpdates)
        hideLoader(loader)
    }
    fun tradertotal(userId: String?) {
        eventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                info("Firebase Donation error : ${error.message}")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                children.forEach {
                }
                app.database.child("user-traders").child(userId!!)
                    .addValueEventListener(eventListener)
            }
        }
    }
}
