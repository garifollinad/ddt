package com.example.ddtapp.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ddtapp.R
import com.example.ddtapp.di.Injectable
import com.example.ddtapp.model.House
import com.example.ddtapp.ui.menu.MenuActivity
import com.example.ddtapp.utils.Constants
import com.example.ddtapp.utils.Screen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class MainFragment : Fragment(), Injectable {

    private lateinit var emptyContainer: ConstraintLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var search: SearchView
    private var fusedLocationClient: FusedLocationProviderClient? = null

    companion object {
        fun newInstance(data: Bundle? = null): MainFragment =
            MainFragment().apply {
                arguments = data
            }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setData()
        setAdapter()
        askPermission()
    }

    // Set location data during first launch after requesting permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty()) {
                val fineLocationGranted = ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                val coarseLocationGranted = ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

                if (fineLocationGranted && coarseLocationGranted) {
                    fusedLocationClient?.lastLocation?.addOnCompleteListener { result ->
                        val location = result.result
                        val currentLocation = LatLng(location.latitude, location.longitude)
                        houseAdapter.setDistance(currentLocation)
                    }
                }
            }
            return
        }
    }

    private fun bindViews(view: View) = with(view) {
        emptyContainer = findViewById(R.id.emptyContainer)
        recyclerView = findViewById(R.id.recyclerView)
        search = findViewById(R.id.search)

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.getContext(),
            layoutManager.getOrientation()
        )
        ContextCompat.getDrawable(context, R.drawable.divider_20dp)?.let { drawable ->
            dividerItemDecoration.setDrawable(drawable)
        }
        recyclerView.addItemDecoration(dividerItemDecoration)
        //used to get current location of user
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private val onHouseDetailListener: OnHouseDetailListener = object : OnHouseDetailListener {
        override fun onHouseClick(model: House?, distance: String?){
            val bundle = bundleOf(Constants.HOUSE_DISTANCE to distance, Constants.HOUSE_ID to model?.id)
            (requireActivity() as MenuActivity).navigateTo(
                fragment = MainDetailFragment.newInstance(bundle),
                tag = Screen.MAIN_DETAIL.name,
                addToStack = true
            )
        }

        //showing default view if filtered house data is not found
        override fun onFilteredResult(isEmpty: Boolean) {
            if (isEmpty) {
                recyclerView.visibility = View.GONE
                emptyContainer.visibility = View.VISIBLE
            } else {
                if (!recyclerView.isVisible) {
                    recyclerView.visibility = View.VISIBLE
                    emptyContainer.visibility = View.GONE
                }
            }
        }
    }

    private val houseAdapter by lazy {
        HouseAdapter(onHouseDetailListener)
    }

    private fun setData() {
        viewModel.getHouses()
        viewModel.liveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is MainViewModel.Result.Houses -> {
                    //setting inital list of houses
                    if (result.housesList.isNotEmpty()) {
                        houseAdapter.initHouses(result.housesList)
                    }
                }
                is MainViewModel.Result.HousesFiltered -> {
                    //setting filtered list of houses using database query
                    houseAdapter.filteredHouses(result.housesList)
                }
                is MainViewModel.Result.HouseModel -> {

                }
                is MainViewModel.Result.Error -> {

                }
            }
        })

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    search.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.search_bottom_sheet_rectangle))
                } else {
                    search.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.search_question_bg))
                }
                // NOTE: (option 1) This is Database search
                val searchList = newText.split(" ")
                if (searchList.size == 3) {
                    val zip = searchList[0] + " " + searchList[1]
                    val city = searchList[2]
                    //searching list of houses based on both zip and city parameters
                    viewModel.getHousesZipAndCityFiltered(zip, city)
                } else {
                    //searching list of houses based on either zip or city parameter
                    viewModel.getHousesZipOrCityFiltered(newText)
                }
                // NOTE: (option 2) This is custom search using custom Filter
                // houseAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun setAdapter() {
        recyclerView.adapter = houseAdapter
    }

    // Request location permission during first launch, if granted set location data
    private fun askPermission() {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (fineLocationGranted && coarseLocationGranted) {
            fusedLocationClient?.lastLocation?.addOnCompleteListener { result ->
                val location = result.result
                val currentLocation = LatLng(location.latitude, location.longitude)
                houseAdapter.setDistance(currentLocation)
            }
        } else {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                Constants.LOCATION_PERMISSION_REQUEST
            )
        }
    }
}