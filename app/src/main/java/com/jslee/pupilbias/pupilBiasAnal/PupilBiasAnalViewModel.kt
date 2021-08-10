package com.jslee.pupilbias.pupilBiasAnal

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jslee.pupilbias.data.AppRepository
import com.jslee.pupilbias.data.constant.AppDataConstants
import com.jslee.pupilbias.data.vo.IrisImage
import com.jslee.pupilbias.pupilSeg.AutoSetPupilAndIris
import javax.inject.Inject

class PupilBiasAnalViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _irisImage = MutableLiveData<IrisImage>()
    val irisImage: LiveData<IrisImage>
        get() = _irisImage

    private val _radiusAnalBitmap = MutableLiveData<Bitmap>()
    val radiusAnalBitmap: LiveData<Bitmap>
        get() = _radiusAnalBitmap

    private val _centerAnalBitmap = MutableLiveData<Bitmap>()
    val centerAnalBitmap: LiveData<Bitmap>
        get() = _centerAnalBitmap


    private val _fourSectorAnalBitmap = MutableLiveData<Bitmap>()
    val fourSectorAnalBitmap: LiveData<Bitmap>
        get() = _fourSectorAnalBitmap

    private val _twelveAnalBitmap = MutableLiveData<Bitmap>()
    val twelveAnalBitmap: LiveData<Bitmap>
        get() = _twelveAnalBitmap

    /** 다음 버튼 클릭 여부 */
    private val _isClickedNextBtn = MutableLiveData<IrisImage>()
    val isClickedNextBtn: LiveData<IrisImage>
        get() = _isClickedNextBtn

    fun start (irisImage: IrisImage) {
        _irisImage.value = irisImage

        val autoSetPupilAndIris = AutoSetPupilAndIris()

        /** 1. 지름 분석 : 동공을 둘러싼 직사각형의 가로 및 세로의 크기와 예측원 반지름 크기 비교 */
        // Rect width, Height vs Pupil Radius
        val bitmapDrawable = _irisImage.value!!.maskImg as BitmapDrawable
        var pupilMaskBitmap: Bitmap = bitmapDrawable.bitmap


        pupilMaskBitmap = autoSetPupilAndIris.drawCircle(_irisImage.value!!.pupilCenter, pupilMaskBitmap,
            _irisImage.value!!.pupilRadius, AppDataConstants.pupilCircleColor)

        // drawRadius(point: Point, pupilMaskBitmap: Bitmap, width:Int, height:Int): Bitmap

        val bitmap: Bitmap = autoSetPupilAndIris.drawRadius(_irisImage.value!!.pupilCenter, pupilMaskBitmap,
            20,30, AppDataConstants.pupilRectColor)

        _radiusAnalBitmap.value = bitmap

        /** 2. 중심 분석 : 동공을 둘러싼 직사각형의 중심 좌표와 동공 영역의 무게중심(1차 모멘텀)좌표를 비교 */
        // Rect 중심, Pupil 중심

        _centerAnalBitmap.value = bitmap

        /** 3. Four Sector */
        // 각도 별로 동공 영역을 자른 후 해당하는 픽셀수를 표시
        // drawArc(point: Point, pupilMaskBitmap: Bitmap, radius:Int, scalar: Scalar, startAngle: Double, endAngle:Double)
        _fourSectorAnalBitmap.value = bitmap


        /** 4. Twelve Sector */
        // 각도 별로 동공 영역을 자른 후 해당하는 픽셀수를 표시
        _twelveAnalBitmap.value = bitmap

    }

    /** 버튼 클릭 시 수행되는 함수들 */
    fun onClickedNextBtn(){
        _isClickedNextBtn.value = irisImage.value
    }

}