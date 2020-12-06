package com.udacity.nano.popularmovies.ui.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.udacity.nano.popularmovies.R
import com.udacity.nano.popularmovies.data.source.PopularMovie
import com.udacity.nano.popularmovies.data.source.remote.model.Genre
import com.udacity.nano.popularmovies.databinding.FragmentDetailBinding
import com.udacity.nano.popularmovies.ui.base.BaseFragment
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

        viewModel.genres.observe(viewLifecycleOwner, Observer<List<Genre>> { data ->
            if (!data.isNullOrEmpty()) {
                val chipGroup = binding.genreList
                val inflator = LayoutInflater.from(chipGroup.context)

                val children = data.map { genre ->
                    val chip = inflator.inflate(R.layout.genre, chipGroup, false) as Chip
                    chip.text = genre.name
                    chip.tag = genre.name
                    chip
                }

                chipGroup.removeAllViews()
                for (chip in children) {
                    chipGroup.addView(chip)
                }
            }
        })


        viewModel.loadGenres(selectedMovie)
        return binding.root
    }
}