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
import org.opencv.core.Point
import javax.inject.Inject

class PupilBiasAnalViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {
    /**
     * ImageView 객체 */
    private val _irisImage = MutableLiveData<IrisImage>()
    val irisImage: LiveData<IrisImage>
        get() = _irisImage

    /**
     * ImageView에 보여줄 분석 결과 데이터 */
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

    /**
     * TextView에 보여줄 분석 결과 데이터 */

    private val _radiusAnalString = MutableLiveData<String>()
    val radiusAnalString: LiveData<String>
        get() = _radiusAnalString

    private val _centerAnalString = MutableLiveData<String>()
    val centerAnalString: LiveData<String>
        get() = _centerAnalString

    private val _fourSectorAnalString = MutableLiveData<String>()
    val fourSectorAnalString: LiveData<String>
        get() = _fourSectorAnalString

    private val _twelveAnalString = MutableLiveData<String>()
    val twelveAnalString: LiveData<String>
        get() = _twelveAnalString

    /**
     * 다음 버튼 클릭 여부 */
    private val _isClickedNextBtn = MutableLiveData<IrisImage>()
    val isClickedNextBtn: LiveData<IrisImage>
        get() = _isClickedNextBtn

    private val autoSetPupilAndIris = AutoSetPupilAndIris()

    fun start (irisImage: IrisImage) {
        _irisImage.value = irisImage

        val bitmapDrawable = _irisImage.value!!.maskImg as BitmapDrawable
        val pupilMaskBitmap: Bitmap = bitmapDrawable.bitmap

        _radiusAnalBitmap.value = pupilMaskBitmap
        _centerAnalBitmap.value = pupilMaskBitmap
        _fourSectorAnalBitmap.value = pupilMaskBitmap
        _twelveAnalBitmap.value = pupilMaskBitmap

        _radiusAnalString.value = "_radiusAnalString"
        _centerAnalString.value = "_centerAnalString"
        _fourSectorAnalString.value = "_fourSectorAnalString"
        _twelveAnalString.value = "_twelveAnalString"


        val rect = irisImage.contourPointList?.let { autoSetPupilAndIris.getRectParm(it) }
        _irisImage.value!!.rectStart = rect?.first ?: Point(0.0, 0.0)
        _irisImage.value!!.rectEnd = rect?.second ?: Point(640.0, 480.0)

        getRadiusAnal(pupilMaskBitmap)


    }

    /**
     * 1. 지름 분석 : 동공을 둘러싼 직사각형의 가로 및 세로의 크기와 예측원 반지름 크기 비교 */
    fun getRadiusAnal(bitmap: Bitmap){

        // 1> 지름 분석 Rect width, Height vs Pupil Radius 크기 비교

        _radiusAnalString.value = " rectWidth : " + _irisImage.value!!.rectWidth + " rectHeight : " + _irisImage.value!!.rectHeight
        // 2> 지름 분석 Rect, Circle 이미지로 표현
        val pupilMaskBitmap = autoSetPupilAndIris.drawCircle(_irisImage.value!!.pupilCenter, bitmap,
            _irisImage.value!!.pupilRadius, AppDataConstants.pupilCircleColor)


        val bitmap: Bitmap = autoSetPupilAndIris.drawRadius(pupilMaskBitmap,
            _irisImage.value!!.rectCenter, _irisImage.value!!.rectWidth, _irisImage.value!!.rectHeight,
            AppDataConstants.pupilRectColor)

        _radiusAnalBitmap.value = bitmap

    }


    /**
     * 2. 중심 분석 : 동공을 둘러싼 직사각형의 중심 좌표와 동공 영역의 무게중심(1차 모멘텀)좌표를 비교 */
    fun getCenterAnal(bitmap: Bitmap){

        // Rect 중심, Pupil 중심
        _centerAnalBitmap.value = bitmap
        _centerAnalString.value = "_centerAnalString"

    }

    /**
     * 3. Four Sector */
    fun getFourSectorAAnal(bitmap: Bitmap){

        // 각도 별로 동공 영역을 자른 후 해당하는 픽셀수를 표시
        // drawArc(point: Point, pupilMaskBitmap: Bitmap, radius:Int, scalar: Scalar, startAngle: Double, endAngle:Double)
        _fourSectorAnalBitmap.value = bitmap
        _fourSectorAnalString.value = "_fourSectorAnalString"

    }

    /**
     * 4. Twelve Sector */
    fun getTwelveSectorAnal(bitmap: Bitmap){

        // 각도 별로 동공 영역을 자른 후 해당하는 픽셀수를 표시
        _twelveAnalBitmap.value = bitmap
        _twelveAnalString.value = "_twelveAnalString"

    }

    /** 버튼 클릭 시 수행되는 함수들 */
    fun onClickedNextBtn(){
        _isClickedNextBtn.value = irisImage.value
    }


}