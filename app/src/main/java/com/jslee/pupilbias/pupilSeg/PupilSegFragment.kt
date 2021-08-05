package com.jslee.pupilbias.pupilSeg

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.jslee.pupilbias.MyApplication
import com.jslee.pupilbias.R
import com.jslee.pupilbias.databinding.FragmentPupilSegBinding
import javax.inject.Inject

class PupilSegFragment: Fragment() {

    private lateinit var binding : FragmentPupilSegBinding

    // 지연 주입
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<PupilSegViewModel> { viewModelFactory } // by viewModels()을 사용하여 viewModel 지연생성

    private val args: PupilSegFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Ask Dagger to inject our dependencies
        (requireActivity().application as MyApplication)
            .appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_pupil_seg,container,false)

        setUpBinding()
        setUpView()
        setUpObserver()

        return binding.root
    }

    private fun setUpBinding(){
        // databinding을 위한 viewmodel 셋팅 -VieWModel의 모든 데이터에 바인딩 된 레이아웃 액세스를 허용
        binding.viewModel = viewModel

        //binding의 lifecycle owner로 fragment view를 지정 -> 이로써,,바인딩이 LiveData 업데이트를 관찰 할 수 있도함
        binding.lifecycleOwner = this

        viewModel.start(args.selectedImage)

    }

    private fun setUpView(){

    }

    private fun setUpObserver(){

    }
}