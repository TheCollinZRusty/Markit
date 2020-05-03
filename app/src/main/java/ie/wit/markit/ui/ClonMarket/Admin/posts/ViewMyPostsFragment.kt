package ie.wit.AdminFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import ie.wit.R
import ie.wit.markit.ui.ClonMarket.Admin.main.MainApp
import kotlinx.android.synthetic.main.fragment_view_posts.view.*
import kotlinx.android.synthetic.main.fragment_view_posts.view.recyclerView
import kotlinx.android.synthetic.main.fragment_view_traders.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

open class ViewMyPostsFragment : Fragment(), AnkoLogger,
    PostListener {

    lateinit var app: MainApp
    lateinit var root: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_view_posts, container, false)
        activity?.title = getString(R.string.action_report)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))

        var query = FirebaseDatabase.getInstance()
            .reference
            .child("user-posts").child(app.currentUser.uid)

        var options = FirebaseRecyclerOptions.Builder<ClonTraderModel>()
            .setQuery(query, ClonTraderModel::class.java)
            .setLifecycleOwner(this)
            .build()

        root.recyclerView.adapter = feedAdapter(options, this)

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deletePost((viewHolder.itemView.tag as ClonTraderModel).uid)
                deleteUserPost(app.currentUser!!.uid,
                    (viewHolder.itemView.tag as ClonTraderModel).uid)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onPostClick(viewHolder.itemView.tag as ClonTraderModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.recyclerView)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ViewMyPostsFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    fun deleteUserPost(userId: String, uid: String?) {
        app.database.child("user-posts").child(userId).child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Post error : ${error.message}")
                    }
                })
    }

    fun deletePost(uid: String?) {
        app.database.child("posts").child(uid!!)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.ref.removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        info("Firebase Post error : ${error.message}")
                    }
                })
    }

    override fun onPostClick(post: ClonTraderModel) {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame, EditPostsFragment.newInstance(post))
            .addToBackStack(null)
            .commit()
    }
}
