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
import kotlinx.android.synthetic.main.fragment_trader_edit.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EditTraderFragment : Fragment(), AnkoLogger {

    lateinit var app: MainApp
    lateinit var loader : AlertDialog
    lateinit var root: View
    var editClonTrader: ClonTraderModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp

        arguments?.let {
            editClonTrader = it.getParcelable("editdonation")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_trader_edit, container, false)
        activity?.title = getString(R.string.action_edit_trader)
        loader = createLoader(activity!!)

        root.EditTraderTitle.setText(editClonTrader!!.Title)
        root.EditTraderDesc.setText(editClonTrader!!.Description)
        root.EditTraderNumber.setText(editClonTrader!!.Number)
        root.EditTraderEmail.setText(editClonTrader!!.TraderEmail)
        root.EditTraderStart.setText(editClonTrader!!.TraderStart)
        root.EditTraderEnd.setText(editClonTrader!!.TraderEnd)


//        root.editMessage.setText(editClonTrader!!.message)
//        root.editUpvotes.setText(editClonTrader!!.upvotes.toString())

        root.btnEdit.setOnClickListener {
            showLoader(loader, "Updating Donation on Server...")
            updateTraderData()
            updateTrader(editClonTrader!!.uid, editClonTrader!!)
            updateUsersTrader(app.currentUser!!.uid,
                               editClonTrader!!.uid, editClonTrader!!)
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(clonTrader: ClonTraderModel) =
            EditTraderFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("editdonation",clonTrader)
                }
            }
    }

    fun updateTraderData() {
        editClonTrader!!.Title = root.EditTraderTitle.text.toString()
        editClonTrader!!.Description = root.EditTraderDesc.text.toString()
        editClonTrader!!.Number = root.EditTraderNumber.text.toString()
        editClonTrader!!.TraderEmail = root.EditTraderEmail.text.toString()
        editClonTrader!!.TraderStart = root.EditTraderStart.text.toString()
        editClonTrader!!.TraderEnd = root.EditTraderEnd.text.toString()
//        editClonTrader!!.Upvotes = root.editUpvotes.text.toString().toInt()
    }

    fun updateUsersTrader(userId: String, uid: String?, clonTrader: ClonTraderModel) {
        app.database.child("user-traders").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(clonTrader)
                        activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFrame, ViewTraderFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Donation error : ${error.message}")
                    }
                })
    }

    fun updateTrader(uid: String?, clonTrader: ClonTraderModel) {
        app.database.child("traders").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(clonTrader)
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Donation error : ${error.message}")
                    }
                })
    }
}
