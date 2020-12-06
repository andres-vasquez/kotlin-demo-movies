package com.udacity.nano.popularmovies.ui.login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.udacity.nano.popularmovies.R
import com.udacity.nano.popularmovies.data.source.User
import com.udacity.nano.popularmovies.databinding.FragmentLoginBinding
import com.udacity.nano.popularmovies.ui.base.BaseFragment
import com.udacity.nano.popularmovies.utils.CompressImage
import com.udacity.nano.popularmovies.utils.Constants
import com.udacity.nano.popularmovies.utils.setLocale
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class LoginFragment : BaseFragment() {

    override val viewModel: LoginViewModel by viewModel()
    private val RC_PERMISSIONS = 201
    private val RC_GALLERY = 202

    private var fileCoverPhoto: Uri? = null
    private var languageSelected: String = Constants.DEFAULT_LANGUAGE

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //Load user data
        viewModel.currentUser.observe(viewLifecycleOwner, Observer { binding.user = it })

        //Spinner
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                languageSelected = p0?.selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        //Edit photo
        viewModel.editPhoto.observe(viewLifecycleOwner, Observer {
            if (it) {
                getPhoto()
                viewModel.doneEditingPhoto()
            }
        })

        //Save
        binding.saveButton.setOnClickListener {
            val user = binding.user
            if (user != null) {
                user.language = languageSelected
                fileCoverPhoto?.let { user.photo = fileCoverPhoto.toString() }
                viewModel.validateAndSave(user)
                setLocale(requireActivity(), languageSelected, true)
            }
        }
        return binding.root
    }

    private fun updateUserInBinding() {
        val user = binding.user
        if (user != null && fileCoverPhoto != null) {
            user.photo = fileCoverPhoto.toString()
            binding.user = user
        }
    }

    private fun getPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (hasPermissions()) {
                openGallery()
            } else {
                askPermissions()
            }
        } else {
            openGallery()
        }
    }

    private fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun askPermissions() {
        requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), RC_PERMISSIONS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RC_PERMISSIONS -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            }
            else -> viewModel.showToast.value = getString(R.string.permissions_error)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_GALLERY && data != null && data.data != null) {
                val image = data.data
                fileCoverPhoto = CompressImage.compressImage(image, requireContext())
                updateUserInBinding()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, getString(R.string.pick_image)), RC_GALLERY
        )
    }
}