package com.example.foursquaretest.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.foursquaretest.ui.main.FavoritesFragment
import com.example.foursquaretest.ui.main.MapFragment
import com.example.foursquaretest.ui.main.PlacesListFragment

class CollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        // Return a NEW fragment instance in createFragment(int)

            0 ->{
                PlacesListFragment()
            }
            1 -> {
                MapFragment()
            }
            else -> {
                FavoritesFragment()
            }
    }


}