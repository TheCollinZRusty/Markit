package ie.wit.markit.ui.ClonMarket.ui_user.ClonTrader

import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_clon_trader.view.recyclerView
import kotlinx.android.synthetic.main.fragment_view_posts.view.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ie.wit.AdminFragment.*
import ie.wit.R
import kotlinx.android.synthetic.main.fragment_view_traders.view.*
import org.jetbrains.anko.info

class ClonTraderFragment : ViewTraderFragment(),
    TraderListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_view_traders, container, false)
//        activity?.title = getString(R.string.menu_report_all)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))

        var query = FirebaseDatabase.getInstance()
            .reference.child("traders")

        var options = FirebaseRecyclerOptions.Builder<ClonTraderModel>()
            .setQuery(query, ClonTraderModel::class.java)
            .setLifecycleOwner(this)
            .build()

        root.recyclerView.adapter = traderAdapter(options, this)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ViewTraderAllFragment().apply {
                arguments = Bundle().apply { }
            }
    }
}