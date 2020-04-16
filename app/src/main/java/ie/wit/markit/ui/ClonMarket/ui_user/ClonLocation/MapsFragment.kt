package ie.wit.fragments

import android.os.Bundle
import com.google.android.gms.maps.*
import ie.wit.markit.ui.ClonMarket.Admin.helpers.getAllDonations
import ie.wit.markit.ui.ClonMarket.Admin.helpers.setMapMarker
import ie.wit.markit.ui.ClonMarket.Admin.helpers.trackLocation
import ie.wit.markit.ui.ClonMarket.Admin.main.MainApp


class MapsFragment : SupportMapFragment(), OnMapReadyCallback{

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        app.mMap = googleMap
        app.mMap.isMyLocationEnabled = true
        app.mMap.uiSettings.isZoomControlsEnabled = true
        app.mMap.uiSettings.setAllGesturesEnabled(true)
        app.mMap.clear()
        trackLocation(app)
        setMapMarker(app)
        getAllDonations(app)
    }
}