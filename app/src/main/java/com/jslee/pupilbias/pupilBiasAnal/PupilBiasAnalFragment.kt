package com.jslee.pupilbias.pupilBiasAnal

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jslee.pupilbias.MyApplication
import com.jslee.pupilbias.R
import com.jslee.pupilbias.data.constant.AppDataConstants
import com.jslee.pupilbias.databinding.FragmentPupilBiasAnalBinding
import com.jslee.pupilbias.pupilSeg.AutoSetPupilAndIris
import javax.inject.Inject

class PupilBiasAnalFragment: Fragment(){

    private lateinit var binding : FragmentPupilBiasAnalBinding

    // 지연 주입
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<PupilBiasAnalViewModel> { viewModelFactory } // by viewModels()을 사용하여 viewModel 지연생성

    private val args: PupilBiasAnalFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Ask Dagger to inject our dependencies
        (requireActivity().application as MyApplication)
            .appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_pupil_bias_anal,container,false)

        setUpBinding()
        setUpView()
        setUpObserver()

        return binding.root
    }

    private fun setUpBinding(){
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.start(args.selectedImage)
    }

    private fun setUpView(){


    }

    private fun setUpObserver(){

        viewModel.isClickedNextBtn.observe(viewLifecycleOwner, Observer {
            if( it != null ){
                this.findNavController().navigate(
                    PupilBiasAnalFragmentDirections.actionPupilBiasAnalFragmentToPupilResultFragment(it)
                )
            }
        })
    }
}