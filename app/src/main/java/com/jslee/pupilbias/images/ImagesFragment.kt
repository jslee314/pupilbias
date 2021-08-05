package com.jslee.pupilbias.images

import android.content.Context
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
import com.jslee.pupilbias.MyApplication
import com.jslee.pupilbias.R
import com.jslee.pupilbias.databinding.FragmentImagesBinding
import javax.inject.Inject

class ImagesFragment: Fragment() {

    private lateinit var binding : FragmentImagesBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<ImagesViewModel> { viewModelFactory } // by viewModels()을 사용하여 viewModel 지연생성

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.start(resources.assets)
    }

    private fun setUpView(){

        // Sets the adapter of the photosGrid RecyclerView
        binding.photosGrid.adapter = PhotoGridAdapter(
            propertyOnClickListener = PhotoGridAdapter.MarsOnClickListener(
                clkListener = viewModel::onClickedNextBtn // ::(리플렉션) : 내가 참조하려는 클래스 혹은 메소드을 찾기위해 사용
            ))
    }

    private fun setUpObserver(){

        viewModel.isClickedNextBtn.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                this.findNavController().navigate(
                    ImagesFragmentDirections.actionImagesFragmentToPupilSegFragment(it)
                )
                viewModel.displayPropertyDetailsComplete()
            }
        })

    }
}