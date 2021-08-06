package com.jslee.pupilbias.pupilSeg

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
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
import org.opencv.core.Mat
import tensorflowlite.model.SegmentationModelExecutor
import javax.inject.Inject


class PupilSegFragment: Fragment() {

    private lateinit var binding : FragmentPupilSegBinding

    // 지연 주입
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<PupilSegViewModel> { viewModelFactory } // by viewModels()을 사용하여 viewModel 지연생성

    private val args: PupilSegFragmentArgs by navArgs()

    private lateinit var mContext: Context
    private var pupilSegmentationModel: SegmentationModelExecutor? = null


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

        // 동공, 홍채의 인공지능 Segmentation 결과 가져오기


        // 동공, 홍채의 인공지능 Segmentation 결과 가져오기
        pupilSegmentationModel = SegmentationModelExecutor(
            mContext,
            SegmentationModelExecutor.PUPIL_MODEL,
            false
        )
        val bitmapDrawable = viewModel.irisImage.value!!.imgSrcUrl as BitmapDrawable

        val (bitmapResult, _, bitmapMaskOnly) = pupilSegmentationModel!!.execute(
            bitmapDrawable.bitmap,
            SegmentationModelExecutor.PUPIL_MODEL
        )

        if (pupilSegmentationModel != null) {
            pupilSegmentationModel!!.close()
            pupilSegmentationModel = null
        }

        val resources: Resources = this.resources
        val maskDrawable = BitmapDrawable(resources, bitmapMaskOnly)
        viewModel.irisImage.value!!.bitmapMaskOnly = maskDrawable
        val maskWidth: Int = viewModel.irisImage.value!!.imgWidth
        val maskHeight: Int = viewModel.irisImage.value!!.imgHeight


        // [STEP 1]: 동공마스크의 무게중심 구하기
//        val centroid: Point = getPupilCenter(bitmapMaskOnly, maskWidth, maskHeight)
//        viewModel.irisImage.value!!.pupilCenterX = centroid.x
//        viewModel.irisImage.value!!.pupilCenterY = centroid.y



//        // [STEP 2]: 동공마스크의 예측원 반지름 구하기
//        // getBitmapMaskOnly: Pupil Mask Image.
//        // getBitmapResult: Iris + Pupil Mask Image.
//
//        // [STEP 2]: 동공마스크의 예측원 반지름 구하기
//        // getBitmapMaskOnly: Pupil Mask Image.
//        // getBitmapResult: Iris + Pupil Mask Image.
//        irisImgInfo.setPpR(setPAndI.getRadius(irisImgInfo, bitmapMaskOnly, maskWidth, maskHeight))
//        irisImgInfo.setIrisR(
//            setPAndI.getRadius(
//                irisImgInfo,
//                bitmapResult, maskWidth, maskHeight
//            )
//        )
//        irisImgInfo.setAnwR(setPAndI.getAutoNerveWaveRadious(irisImgInfo))


    }

    private fun setUpObserver(){

    }
}