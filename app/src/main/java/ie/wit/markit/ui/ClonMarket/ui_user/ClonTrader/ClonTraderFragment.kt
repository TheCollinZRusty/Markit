package ie.wit.markit.ui.ClonMarket.ui_user.ClonTrader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.R
import ie.wit.markit.ui.ClonMarket.Admin.main.MainApp
import kotlinx.android.synthetic.main.fragment_clon_trader.view.*


class ClonTraderFragment : Fragment() {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var root = inflater.inflate(R.layout.fragment_clon_trader, container, false)
        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))
//        root.recyclerView.adapter = TraderAdapter(app.clonTraders.findAll())
        return root
    }
}