package ie.wit.AdminFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import ie.wit.markit.ui.ClonMarket.Admin.main.MainApp
import kotlinx.android.synthetic.main.fragment_add_trader.*
import kotlinx.android.synthetic.main.fragment_add_trader.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.util.*
import java.text.SimpleDateFormat


class AddTraderFragment : Fragment(), AnkoLogger {

    lateinit var app: MainApp
    lateinit var loader: AlertDialog
    lateinit var eventListener: ValueEventListener
    var trader = ClonTraderModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(ie.wit.R.layout.fragment_add_trader, container, false)
        loader = createLoader(activity!!)
        activity?.title = getString(ie.wit.R.string.action_add_trader)


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
        layout.btnAdd.setOnClickListener {


            val sdf = SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z")
            val currentTime = sdf.format(Date())

            val title = layout.traderTitle.text.toString()
            val description = layout.traderDesc.text.toString()
            val number = layout.traderNumber.text.toString()
            val traderemail = layout.traderEmail.text.toString()
            val traderstart = layout.traderStart.text.toString()
            val traderend = layout.traderEnd.text.toString()
            if (title.isNotEmpty() && description.isNotEmpty() && number.isNotEmpty() && traderemail.isNotEmpty() && traderstart.isNotEmpty() && traderend.isNotEmpty()) {
                writeNewTrader(
                    ClonTraderModel(
                        Title = title,
                        CurrentTime = currentTime,
                        Description = description,
                        Number = number,
                        TraderEmail = traderemail,
                        TraderStart = traderstart,
                        TraderEnd = traderend,
                        profilepic = app.userImage.toString(),
                        email = app.currentUser?.email

                    )
                )
                Toast.makeText(activity,"Info entered successfully",Toast.LENGTH_SHORT).show()

            }
            else {
                Toast.makeText(activity,"please fill all info!",Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        tradertotal(app.currentUser?.uid)
    }

    override fun onPause() {
        super.onPause()
        if (app.currentUser.uid != null)
            app.database.child("user-traders")
                .child(app.currentUser!!.uid)
                .removeEventListener(eventListener)
    }

    fun writeNewTrader(clonTrader: ClonTraderModel) {
//         Create new clonTrader at /clonTraders & /clonTraders/$uid
        showLoader(loader, "Adding Trader to Firebase")
        info("Firebase DB Reference : $app.database")
        val uid = app.currentUser!!.uid
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
                info("Firebase Trader error : ${error.message}")
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
