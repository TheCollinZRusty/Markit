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
import kotlinx.android.synthetic.main.fragment_add_post.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.HashMap


class AddPostFragment : Fragment(), AnkoLogger {

    lateinit var app: MainApp
    var favourite = false
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

        val root = inflater.inflate(R.layout.fragment_add_post, container, false)
        loader = createLoader(activity!!)
        activity?.title = getString(R.string.action_add_trader)


        setButtonListener(root)
        return root;
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddPostFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    fun setButtonListener(layout: View) {
        layout.AddButton.setOnClickListener {
            val post_Title = layout.Post_title.text.toString()
            val description = layout.Description.text.toString()
            writeNewPost(
                ClonTraderModel(
                    Post_Title = post_Title,
                    PostBody = description,
                    profilepic = app.userImage.toString(),
                    isfavourite = favourite,
                    email = app.auth.currentUser?.email
                )
            )
        }
    }
    override fun onResume() {
        super.onResume()
        posttotal(app.auth.currentUser?.uid)
    }

    override fun onPause() {
        super.onPause()
        if (app.auth.uid != null)
            app.database.child("user-posts")
                .child(app.auth.currentUser!!.uid)
                .removeEventListener(eventListener)
    }

    fun writeNewPost(clonTrader: ClonTraderModel) {
//         Create new clonTrader at /clonPosts & /clonPosts/$uid
        showLoader(loader, "Adding Donation to Firebase")
        info("Firebase DB Reference : $app.database")
        val uid = app.auth.currentUser!!.uid
        val key = app.database.child("posts").push().key
        if (key == null) {
            info("Firebase Error : Key Empty")
            return
        }
        clonTrader.uid = key
        val traderValues = clonTrader.toMap()
        val childUpdates = HashMap<String, Any>()
        childUpdates["/posts/$key"] = traderValues
        childUpdates["/user-posts/$uid/$key"] = traderValues
        app.database.updateChildren(childUpdates)
        hideLoader(loader)
    }
    fun posttotal(userId: String?) {
        eventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                info("Firebase posts error : ${error.message}")
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                children.forEach {
                }
                app.database.child("user-posts").child(userId!!)
                    .addValueEventListener(eventListener)
            }
        }
    }
}
