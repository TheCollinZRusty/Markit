package ie.wit.markit.ui.ClonMarket.ui_user.ClonLocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import ie.wit.R
import ie.wit.markit.ui.ClonMarket.Admin.main.MainApp

class ClonLocation : Fragment() {

    private lateinit var Map: GoogleMap
    lateinit var app: MainApp
    lateinit var root : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_clon_location, container, false)
        activity?.title = getString(R.string.action_add_trader)
        return root;
    }
}


