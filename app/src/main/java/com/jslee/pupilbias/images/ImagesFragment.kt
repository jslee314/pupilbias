package com.jslee.pupilbias.images

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.jslee.pupilbias.MyApplication
import com.jslee.pupilbias.R
import com.jslee.pupilbias.databinding.FragmentImagesBinding
import javax.inject.Inject

class ImagesFragment: Fragment() {

    private lateinit var binding : FragmentImagesBinding

    // 지연 주입
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<ImagesViewModel> { viewModelFactory } // by viewModels()을 사용하여 viewModel 지연생성

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Ask Dagger to inject our dependencies
        (requireActivity().application as MyApplication)
            .appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_images,container,false)

        setUpBinding()
        setUpView()
        setUpObserver()

        return binding.root
    }

    private fun setUpBinding(){

    }

    private fun setUpView(){

    }

    private fun setUpObserver(){

    }
}