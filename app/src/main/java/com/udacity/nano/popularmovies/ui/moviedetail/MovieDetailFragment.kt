package com.udacity.nano.popularmovies.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.udacity.nano.popularmovies.data.source.PopularMovie
import com.udacity.nano.popularmovies.databinding.FragmentDetailBinding
import com.udacity.nano.popularmovies.databinding.FragmentSplashBinding
import com.udacity.nano.popularmovies.ui.base.BaseFragment
import com.udacity.nano.popularmovies.ui.moviedetail.MovieDetailFragmentArgs.Companion.fromBundle
import com.udacity.nano.popularmovies.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailFragment : BaseFragment() {
    override val viewModel: MovieDetailViewModel by viewModel()

    private val selectedMovie: PopularMovie by lazy {
        val args = MovieDetailFragmentArgs.fromBundle(requireArguments())
        args.movie
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.selectedMovie = selectedMovie
        return binding.root
    }
}