package com.udacity.nano.popularmovies.ui.movielist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.nano.popularmovies.data.source.PopularMovie
import com.udacity.nano.popularmovies.databinding.ListItemBinding

class MovieListAdapter(val clickListener: MovieClickListener) :
    ListAdapter<PopularMovie, MovieListAdapter.AsteroidListViewHolder>(DiffCallback) {
    companion object DiffCallback : DiffUtil.ItemCallback<PopularMovie>() {
        override fun areItemsTheSame(oldItem: PopularMovie, newItem: PopularMovie): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PopularMovie, newItem: PopularMovie): Boolean {
            return oldItem == newItem
        }
    }

    class AsteroidListViewHolder(private var binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listener: MovieClickListener, movie: PopularMovie) {
            binding.movie = movie
            binding.clickListener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AsteroidListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBinding.inflate(layoutInflater, parent, false)
                return AsteroidListViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidListViewHolder {
        return AsteroidListViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: AsteroidListViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }
}

class MovieClickListener(val clickListener: (movie: PopularMovie) -> Unit) {
    fun onClick(movie: PopularMovie) = clickListener(movie)
}