package com.example.foursquaretest.ui.main


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.example.foursquaretest.R
import com.example.foursquaretest.databinding.FragmentFavoritesBinding
import com.example.foursquaretest.ui.main.viewmodel.NearbyPlacesViewModel
import com.example.foursquaretest.utils.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoritesFragment : BaseFragment(R.layout.fragment_favorites) {

    private lateinit var binding : FragmentFavoritesBinding

    private val viewModel: NearbyPlacesViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFavorites()
        setupClickObserver()

    }

    private fun setupClickObserver(){
        viewModel.actionDetailsFavorite.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { action ->
                if (action) {
                    val intent = Intent(requireContext(), FavoriteDetailActivity::class.java)
                    intent.putExtra("place",viewModel.favoritePlace.value)
                    startActivity(intent)
                }
            }
        }
    }
}