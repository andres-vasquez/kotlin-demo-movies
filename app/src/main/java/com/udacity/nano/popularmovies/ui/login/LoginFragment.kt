package com.udacity.nano.popularmovies.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.nano.popularmovies.data.source.MovieRepositoryI
import com.udacity.nano.popularmovies.databinding.FragmentLoginBinding
import com.udacity.nano.popularmovies.databinding.FragmentSplashBinding
import com.udacity.nano.popularmovies.ui.base.BaseFragment
import com.udacity.nano.popularmovies.ui.splash.SplashViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment() {

    override val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }
}