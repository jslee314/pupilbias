package com.jslee.pupilbias.pupilSeg

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
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
import com.jslee.pupilbias.data.constant.ViewStatus
import com.jslee.pupilbias.data.vo.IrisImage
import com.jslee.pupilbias.databinding.FragmentPupilSegBinding
import org.opencv.core.Point
import tensorflowlite.data.ModelExecutionResultVO
import tensorflowlite.model.SegmentationModelExecutor
import javax.inject.Inject

class PupilSegFragment: Fragment() {

    private lateinit var binding : FragmentPupilSegBinding

    // 지연 주입
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<PupilSegViewModel> { viewModelFactory } // by viewModels()을 사용하여 viewModel 지연생성

    private val args: PupilSegFragmentArgs by navArgs()
    private val autoSetPupilAndIris = AutoSetPupilAndIris()
    lateinit var resizedBitmap:Bitmap
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
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
        val bitmapDrawable = viewModel.irisImage.value!!.imgSrcUrl as BitmapDrawable

        // 동공, 홍채의 인공지능 Segmentation 결과 가져오기
        var pupilSegmentationModel = SegmentationModelExecutor(mContext, SegmentationModelExecutor.PUPIL_MODEL, false)
        val pupilExecutionResultVO: ModelExecutionResultVO = pupilSegmentationModel!!.execute(bitmapDrawable.bitmap, SegmentationModelExecutor.PUPIL_MODEL)

        if (pupilSegmentationModel != null) {
            pupilSegmentationModel!!.close()
        }

        resizedBitmap = Bitmap.createScaledBitmap(pupilExecutionResultVO.bitmapMaskOnly, viewModel.irisImage.value!!.imgWidth, viewModel.irisImage.value!!.imgHeight, true)

        val maskDrawable = BitmapDrawable(this.resources, resizedBitmap)
        viewModel.irisImage.value!!.bitmapMaskOnly = maskDrawable
        viewModel.irisImage.value!!.segmentationLog = pupilExecutionResultVO.executionLog

    }

    private fun setUpObserver() {

        viewModel.isClickedSegPupilBtn.observe(viewLifecycleOwner, Observer {
            if ( it == true ) {
                viewModel.irisImage.value!!.viewSeg = ViewStatus.VISIBLE
                Log.d("jjslee", "viewModel.irisImage.value!!.viewSeg  :  " + viewModel.irisImage.value!!.viewSeg)
            }
        })

        viewModel.isClickedCenterBtn.observe(viewLifecycleOwner, Observer {
            if ( it == true ) {
                // [STEP 1]: 동공마스크의 무게중심 구하기
                val autoSetPupilAndIris = AutoSetPupilAndIris()
                val centroid: Point = autoSetPupilAndIris.getPupilCenter(resizedBitmap)!!
                viewModel.irisImage.value!!.pupilCenterX = centroid.x.toInt()
                viewModel.irisImage.value!!.pupilCenterY = centroid.y.toInt()
                Log.d("jjslee", "pupilCenterX : " + centroid.x.toInt() + ",  pupilCenterY : " + centroid.y.toInt())
            }
        })

        viewModel.isClickedCircleBtn.observe(viewLifecycleOwner, Observer {
            if ( it == true ) {

                // [STEP 2]: 동공마스크의 예측원 반지름 구하기
                viewModel.irisImage.value!!.pupilRadius = autoSetPupilAndIris.getRadius(viewModel.irisImage.value!!, resizedBitmap)
                Log.d("jjslee", "pupilRadius : " + viewModel.irisImage.value!!.pupilRadius)
            }
        })

        viewModel.isClickedNextBtn.observe(viewLifecycleOwner, Observer {
            if ( it == true ) {
                this.findNavController().navigate(
                    PupilSegFragmentDirections.actionPupilSegFragmentToPupilBiasAnalFragment()
                )
            }
        })


    }
}