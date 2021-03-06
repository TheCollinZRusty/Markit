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
import kotlinx.android.synthetic.main.fragment_edit_post.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class EditPostsFragment : Fragment(), AnkoLogger {

    lateinit var app: MainApp
    lateinit var loader : AlertDialog
    lateinit var root: View
    var editClonTrader: ClonTraderModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp

        arguments?.let {
            editClonTrader = it.getParcelable("editposts")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_edit_post, container, false)
        activity?.title = getString(R.string.action_edit)
        loader = createLoader(activity!!)
        root.editPost_title.setText(editClonTrader!!.Post_Title)
        root.editDescription.setText(editClonTrader!!.PostBody)

        root.editUpdateButton.setOnClickListener {
            showLoader(loader, "Updating posts on Server...")
            updatePostData()
            updatePost(editClonTrader!!.uid, editClonTrader!!)
            updateUserPost(app.currentUser!!.uid,
                               editClonTrader!!.uid, editClonTrader!!)
        }

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance(clonTrader: ClonTraderModel) =
            EditPostsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("editposts",clonTrader)
                }
            }
    }

    fun updatePostData() {
//        editClonTrader!!.Name = root.editName.text.toString()
        editClonTrader!!.PostBody = root.editDescription.text.toString()
//        editClonTrader!!.Number = root.editNumber.text.toString()
//        editClonTrader!!.message = root.editMessage.text.toString()
//        editClonTrader!!.upvotes = root.editUpvotes.text.toString().toInt()
    }

    fun updateUserPost(userId: String, uid: String?, clonTrader: ClonTraderModel) {
        app.database.child("user-posts").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(clonTrader)
                        activity!!.supportFragmentManager.beginTransaction()
                        .replace(R.id.homeFrame, ViewMyPostsFragment.newInstance())
                        .addToBackStack(null)
                        .commit()
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Trader error : ${error.message}")
                    }
                })
    }

    fun updatePost(uid: String?, clonTrader: ClonTraderModel) {
        app.database.child("posts").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.setValue(clonTrader)
                        hideLoader(loader)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Trader error : ${error.message}")
                    }
                })
    }
}
