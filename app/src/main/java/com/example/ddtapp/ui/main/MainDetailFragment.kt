package com.example.ddtapp.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.ddtapp.BuildConfig
import com.example.ddtapp.R
import com.example.ddtapp.di.Injectable
import com.example.ddtapp.model.House
import com.example.ddtapp.ui.menu.MenuActivity
import com.example.ddtapp.utils.Constants
import com.example.ddtapp.utils.LocationUtils
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainDetailFragment : Fragment(), Injectable, OnMapReadyCallback {

    private var modelHouse: House? = null
    private var distance: String? = ""
    private lateinit var searchToolbar: Toolbar
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var expandedImage: ImageView
    private lateinit var price: TextView
    private lateinit var bed: TextView
    private lateinit var bath: TextView
    private lateinit var size: TextView
    private lateinit var location: TextView
    private lateinit var description: TextView

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        modelHouse = args?.get(Constants.HOUSE_DATA) as House
        distance = args.getString(Constants.HOUSE_DISTANCE)
    }

    companion object {
        fun newInstance(data: Bundle? = null): MainDetailFragment =
            MainDetailFragment().apply {
                arguments = data
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        setData()
    }

    private fun bindViews(view: View) = with(view) {
        expandedImage = findViewById(R.id.expandedImage)
        price = findViewById(R.id.price)
        bed = findViewById(R.id.bed)
        bath = findViewById(R.id.bath)
        size = findViewById(R.id.size)
        location = findViewById(R.id.location)
        description = findViewById(R.id.description)
        searchToolbar = findViewById(R.id.searchToolbar)
    }

    private fun setData() {
        price.text = resources.getString(R.string.usd_sign) + LocationUtils.formatDecimalSeparator(modelHouse?.price)
        bed.text = modelHouse?.bedrooms.toString()
        bath.text = modelHouse?.bathrooms.toString()
        size.text = modelHouse?.size.toString()
        location.text = distance + resources.getString(R.string.km)
        description.text = modelHouse?.description
        Glide.with(this)
            .load(BuildConfig.BASE_URL + modelHouse?.image)
            .placeholder(R.drawable.ic_home)
            .into(expandedImage)
        searchToolbar.setOnClickListener {
            (activity as MenuActivity).onBackPressed()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val latLng = LatLng(modelHouse?.latitude?.toDouble()!!, modelHouse?.longitude?.toDouble()!!)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.5f))
        map.addMarker(MarkerOptions().position(latLng))
        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMapClickListener {
            val url = Uri.parse("google.navigation:q=${it.latitude},${it.longitude}")
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
        mapView.onResume()
    }
}