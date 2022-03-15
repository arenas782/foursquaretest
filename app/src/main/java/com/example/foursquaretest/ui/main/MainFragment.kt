package com.example.foursquaretest.ui.main

import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.example.foursquaretest.R
import com.example.foursquaretest.databinding.FragmentMainBinding
import com.example.foursquaretest.ui.main.viewmodel.NearbyPlacesViewModel
import com.example.foursquaretest.utils.BaseFragment
import com.example.foursquaretest.utils.Commons
import com.example.foursquaretest.utils.Commons.hideKeyboard
import com.example.foursquaretest.utils.Commons.textChanges
import com.example.foursquaretest.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainFragment : BaseFragment(R.layout.fragment_main) {

    private lateinit var binding : FragmentMainBinding
    private lateinit var viewModel : NearbyPlacesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this)[NearbyPlacesViewModel::class.java]

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tiQuery.textChanges().debounce(800).onEach {
            if(!it.isNullOrEmpty()){
                Log.e("searching",it.toString())
                viewModel.searchPlaces(it.toString()).observe(viewLifecycleOwner){ response ->
                    when (response.status){
                        Status.SUCCESS -> {
                            binding.rvResults.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.GONE
                            if(response.data?.results?.isEmpty() == true){
                                binding.tvNothingFound.visibility = View.VISIBLE
                            }
                            else{
                                binding.tvNothingFound.visibility = View.GONE
                            }
                        }
                        Status.ERROR -> {
                            binding.rvResults.visibility = View.GONE
                            binding.progressBar.visibility = View.GONE
                            binding.tvNothingFound.visibility = View.GONE
                            hideKeyboard(requireActivity())
                            Commons.showSnackBar(Commons.getString(R.string.something_went_wrong),binding.root)

                        }
                        Status.LOADING -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvResults.visibility = View.GONE
                            binding.tvNothingFound.visibility = View.GONE
                        }
                    }

                }
            }
            else{
                viewModel.clearPlaces()
            }
        }.launchIn(lifecycleScope)
    }







}