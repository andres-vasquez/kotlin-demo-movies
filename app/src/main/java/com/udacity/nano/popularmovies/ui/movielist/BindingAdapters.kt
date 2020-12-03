package com.udacity.nano.popularmovies.ui.movielist

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.udacity.nano.popularmovies.R
import com.udacity.nano.popularmovies.data.source.PopularMovie


@BindingAdapter("moviePicture")
fun bindMovieImage(imageView: ImageView, movie: PopularMovie?) {
    if (movie != null) {
        Picasso.with(imageView.context)
            .load(movie.image)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(imageView, object : Callback {
                override fun onSuccess() {
                    imageView.contentDescription =
                        imageView.context.getString(
                            R.string.nasa_picture_of_day_content_description_format,
                            movie.displayName
                        )
                }

                override fun onError() {
                    imageView.contentDescription =
                        imageView.context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
                }
            })
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<PopularMovie>?) {
    val adapter = recyclerView.adapter as MovieListAdapter
    adapter.submitList(data)
}

@BindingAdapter("loadingVisibility")
fun bindLoadingVisibility(view: View, visible: Boolean) {
    if (visible) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}
