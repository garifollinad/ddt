package com.example.ddtapp.ui.main

import android.location.Location
import android.util.Log
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
    private var currentLocation = LatLng(0.0, 0.0)
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

        fun bindView(item: House) = with(view) {
            val houseItem: ConstraintLayout = findViewById(R.id.houseItem)
            val price: TextView = findViewById(R.id.price)
            val address: TextView = findViewById(R.id.address)
            val bed: TextView = findViewById(R.id.bed)
            val bath: TextView = findViewById(R.id.bath)
            val size: TextView = findViewById(R.id.size)
            val location: TextView = findViewById(R.id.location)
            val houseImg: ImageView = findViewById(R.id.houseImg)

            price.text = resources.getString(R.string.usd_sign) + LocationUtils.formatDecimalSeparator(item.price)
            address.text = item.zip +  " " + item?.city
            bed.text = item.bedrooms.toString()
            bath.text = item.bathrooms.toString()
            size.text = item.size.toString()
            val houseLocation = LatLng(item.latitude.toDouble(), item.longitude.toDouble())
            val distance = LocationUtils.calculateDistance(currentLocation, houseLocation).toString()
            location.text = distance + resources.getString(R.string.km)
            Glide.with(this)
                .load(BuildConfig.BASE_URL + item.image)
                .placeholder(R.drawable.ic_home)
                .into(houseImg)

            houseItem.setOnClickListener {
                onHouseDetailListener.onHouseClick(item, distance)
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
                initialHouses?.let {
                    val items: List<House>? =
                        initialHouses?.filter {
                            it.zip.contains(constraint.toString(), true)
                                    || it.city.contains(constraint.toString(), true)
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