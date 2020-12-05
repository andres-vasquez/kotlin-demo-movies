package com.udacity.nano.popularmovies.ui.movielist

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.udacity.nano.popularmovies.R
import com.udacity.nano.popularmovies.data.source.PopularMovie
import com.udacity.nano.popularmovies.data.source.User

@BindingAdapter("userImage")
fun bindMUserImage(imageView: ImageView, user: User?) {
    if (user != null) {
        Glide.with(imageView)
            .load(user.photo)
            .circleCrop()
            .error(R.drawable.ic_baseline_android_24)
            .into(imageView)
    } else {
        Glide.with(imageView).load(R.drawable.ic_baseline_android_empty).circleCrop()
            .into(imageView)
    }
}

@BindingAdapter("moviePicture")
fun bindMovieImage(imageView: ImageView, movie: PopularMovie?) {
    if (movie != null) {

        Glide.with(imageView)
            .load(movie.image)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    imageView.contentDescription =
                        imageView.context.getString(R.string.movie_description_error)
                    return true
                }

                override fun onResourceReady(
                    resource: Drawable?, model: Any?, target: Target<Drawable>?,
                    dataSource: DataSource?, isFirstResource: Boolean
                ): Boolean {
                    imageView.contentDescription =
                        imageView.context.getString(
                            R.string.movie_description,
                            movie.displayName
                        )
                    return false
                }

            })
            .into(imageView)
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
