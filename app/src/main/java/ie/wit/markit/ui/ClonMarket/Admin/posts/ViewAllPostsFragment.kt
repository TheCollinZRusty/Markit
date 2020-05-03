package ie.wit.AdminFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import ie.wit.R
import kotlinx.android.synthetic.main.fragment_view_posts.view.recyclerView

class ViewAllPostsFragment : ViewMyPostsFragment(),
    PostListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_view_posts, container, false)
//        activity?.title = getString(R.string.menu_report_all)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))

        var query = FirebaseDatabase.getInstance()
            .reference.child("posts")

        var options = FirebaseRecyclerOptions.Builder<ClonTraderModel>()
            .setQuery(query, ClonTraderModel::class.java)
            .setLifecycleOwner(this)
            .build()

        root.recyclerView.adapter = feedAdapter(options, this)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ViewAllPostsFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}