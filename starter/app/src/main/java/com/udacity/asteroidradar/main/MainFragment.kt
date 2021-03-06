package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.models.Asteroid

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: AsteroidAdapter

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        adapter = AsteroidAdapter(AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner) { asteroids ->
            if (asteroids != null) {
                adapter.submitList(asteroids)
            }
        }

        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner) { asteroid ->
            if (asteroid != null) {
                navigateToDetailFragment(asteroid)
                viewModel.onNavigateToDetailFragmentFinish()
            }
        }

        viewModel.pictureOfDay.observe(viewLifecycleOwner) { pictureOfDay ->
            pictureOfDay.let {
                if (pictureOfDay.mediaType == "image") {
                    Picasso.with(context).load(pictureOfDay.url)
                        .into(binding.activityMainImageOfTheDay)
                }
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun navigateToDetailFragment(asteroid: Asteroid) {
        findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
    }

}
