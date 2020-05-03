package ie.wit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import ie.wit.AdminFragment.ClonTraderModel
import ie.wit.AdminFragment.PostListener
import ie.wit.AdminFragment.ViewMyPostsFragment
import ie.wit.AdminFragment.feedAdapter
import ie.wit.R
import ie.wit.markit.ui.ClonMarket.Admin.helpers.getAllPosts
import ie.wit.markit.ui.ClonMarket.Admin.helpers.getFavouritePosts
import ie.wit.markit.ui.ClonMarket.Admin.helpers.setMapMarker
import ie.wit.markit.ui.ClonMarket.Admin.main.MainApp
import kotlinx.android.synthetic.main.fragment_favourites.*
import kotlinx.android.synthetic.main.fragment_view_posts.view.*


class FavouritesFragment : ViewMyPostsFragment(),
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
            .reference.child("posts").orderByChild("isfavourite").equalTo(true)

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
            FavouritesFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}