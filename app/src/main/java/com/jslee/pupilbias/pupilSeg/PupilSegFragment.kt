package com.jslee.pupilbias.pupilSeg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.jslee.pupilbias.R
import com.jslee.pupilbias.databinding.FragmentPupilSegBinding

class PupilSegFragment: Fragment() {
    private lateinit var binding : FragmentPupilSegBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home,container,false)

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