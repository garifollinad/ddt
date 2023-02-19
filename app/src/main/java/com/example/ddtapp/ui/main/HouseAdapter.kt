package com.example.ddtapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ddtapp.BuildConfig
import com.example.ddtapp.R
import com.example.ddtapp.model.House
import com.example.ddtapp.utils.Constants
import com.example.ddtapp.utils.LocationUtils
import com.google.android.gms.maps.model.LatLng

interface OnHouseDetailListener {
    fun onHouseClick(model: House?, distance: String?)
    fun onFilteredResult(isEmpty: Boolean)
}

class HouseAdapter (
    private var onHouseDetailListener: OnHouseDetailListener
): RecyclerView.Adapter<HouseAdapter.HouseViewHolder>(), Filterable {

    private var listHouses: List<House> = arrayListOf()
    private var initialHouses: List<House> = arrayListOf()
    private var currentLocation = LatLng(Constants.DISTANCE_ZERO, Constants.DISTANCE_ZERO)
    private var valueFilter: ValueFilter? = null

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): HouseViewHolder {
        return HouseViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_house_view, parent, false)
        )
    }

    override fun getItemCount(): Int = listHouses.size

    override fun onBindViewHolder(viewHolder: HouseViewHolder, position: Int) {
        viewHolder.bindView(listHouses.get(position))
    }

    inner class HouseViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        private lateinit var houseItem: ConstraintLayout
        private lateinit var price: TextView
        private lateinit var address: TextView
        private lateinit var bed: TextView
        private lateinit var bath: TextView
        private lateinit var size: TextView
        private lateinit var location: TextView
        private lateinit var houseImg: ImageView

        fun bindView(item: House) = with(view) {
            houseItem = findViewById(R.id.houseItem)
            price = findViewById(R.id.price)
            address = findViewById(R.id.address)
            bed = findViewById(R.id.bed)
            bath = findViewById(R.id.bath)
            size = findViewById(R.id.size)
            location = findViewById(R.id.location)
            houseImg = findViewById(R.id.houseImg)
            setData(item)
        }

        private fun setData(item: House) {
            val priceText = view.resources.getString(R.string.usd_sign) + LocationUtils.formatDecimalSeparator(item.price)
            val addressText = item.zip + " " + item.city
            val houseLocation = LatLng(item.latitude.toDouble(), item.longitude.toDouble())
            val distance = LocationUtils.calculateDistance(currentLocation, houseLocation)

            if (distance == Constants.DISTANCE_ZERO) {
                location.visibility = View.GONE
            } else {
                val locationText = distance.toString() + view.resources.getString(R.string.km)
                location.visibility = View.VISIBLE
                location.text = locationText
            }

            price.text = priceText
            address.text = addressText
            bed.text = item.bedrooms.toString()
            bath.text = item.bathrooms.toString()
            size.text = item.size.toString()

            Glide.with(view)
                .load(BuildConfig.BASE_URL + item.image)
                .into(houseImg)

            houseItem.setOnClickListener {
                onHouseDetailListener.onHouseClick(item, distance.toString())
            }
        }
    }

    fun filteredHouses(list: List<House>) {
        listHouses = checkNotNull(list)
        onHouseDetailListener.onFilteredResult(list.isEmpty())
        notifyDataSetChanged()
    }

    fun initHouses(list: List<House>) {
        listHouses = checkNotNull(list)
        initialHouses = checkNotNull(list)
        notifyDataSetChanged()
    }

    fun setDistance(latLng: LatLng) {
        currentLocation = latLng
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        if (valueFilter == null) {
            valueFilter = ValueFilter()
        }
        return valueFilter!!
    }

    private inner class ValueFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            if (constraint != null) {
                //filtering houses based on zip or city
                val filterText = constraint.toString()
                initialHouses.let {
                    val items: List<House> =
                        initialHouses.filter {
                            it.zip.contains(filterText, true)
                                    || it.city.contains(filterText, true)
                        }
                    results.values = items
                }
            }
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredHouses(results?.values as List<House>)
        }
    }
}