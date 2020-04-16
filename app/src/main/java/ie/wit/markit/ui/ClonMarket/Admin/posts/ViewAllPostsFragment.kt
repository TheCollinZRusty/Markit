package ie.wit.AdminFragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import ie.wit.R
import kotlinx.android.synthetic.main.fragment_view_posts.view.*
import org.jetbrains.anko.info

class ViewAllPostsFragment : ViewMyPostsFragment(),
    PostListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_view_posts, container, false)
        activity?.title = getString(R.string.menu_report_all)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        setSwipeRefresh()

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ViewAllPostsFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun setSwipeRefresh() {
        root.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.swiperefresh.isRefreshing = true
                getAllUsersPosts()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getAllUsersPosts()
    }

    fun getAllUsersPosts() {
        loader = createLoader(activity!!)
        showLoader(loader, "Downloading All Users Donations from Firebase")
        val traderList = ArrayList<ClonTraderModel>()
        app.database.child("posts")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Trader error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    hideLoader(loader)
                    val children = snapshot.children
                    children.forEach {
                        val trader = it.
                            getValue<ClonTraderModel>(ClonTraderModel::class.java)

                        traderList.add(trader!!)
                        root.recyclerView.adapter =
                            feedAdapter(
                                traderList,
                                this@ViewAllPostsFragment,
                                true
                            )
                        root.recyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()

                        app.database.child("posts").removeEventListener(this)
                    }
                }
            })
    }
}
