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
import java.lang.Math.round
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

        val maskBitmapDrawable = _irisImage.value!!.maskImg as BitmapDrawable
        val originalBitmapDrawable = _irisImage.value!!.originalImg as BitmapDrawable

        val pupilMaskBitmap: Bitmap = maskBitmapDrawable.bitmap
        val originalMaskBitmap: Bitmap = originalBitmapDrawable.bitmap

        val resizedPupilBitmap: Bitmap = Bitmap.createScaledBitmap(originalMaskBitmap,
            _irisImage.value!!.imgWidth, _irisImage.value!!.imgHeight, true)

        _radiusAnalBitmap.value = resizedPupilBitmap
        _centerAnalBitmap.value = resizedPupilBitmap
        _fourSectorAnalBitmap.value = resizedPupilBitmap
        _twelveAnalBitmap.value = resizedPupilBitmap

        _radiusAnalString.value = "_radiusAnalString"
        _centerAnalString.value = "_centerAnalString"
        _fourSectorAnalString.value = "_fourSectorAnalString"
        _twelveAnalString.value = "_twelveAnalString"

        val rect = irisImage.contourPointList?.let { autoSetPupilAndIris.getRectParm(it) }
        _irisImage.value!!.rectStart = rect?.first ?: Point(0.0, 0.0)
        _irisImage.value!!.rectEnd = rect?.second ?: Point(640.0, 480.0)

        getRadiusAnal(resizedPupilBitmap)
        getCenterAnal(resizedPupilBitmap)

        getFourSectorAnal(pupilMaskBitmap)
        getTwelveSectorAnal(pupilMaskBitmap)

    }

    /**
     * 1. 지름 분석 : 동공을 둘러싼 직사각형의 가로 및 세로의 크기와 예측원 반지름 크기 비교 */
    fun getRadiusAnal(bitmap: Bitmap){

        // 1> 지름 분석 Rect width, Height vs Pupil Radius 크기 비교
        _radiusAnalString.value = " rectWidth : " + _irisImage.value!!.rectWidth +
                "\n rectHeight : " + _irisImage.value!!.rectHeight +
                "\n circleRadius : " + _irisImage.value!!.circleRadius * 2

        if(_irisImage.value!!.rectWidth <(_irisImage.value!!.circleRadius * 2)
            && _irisImage.value!!.rectHeight >(_irisImage.value!!.circleRadius * 2)){
            _radiusAnalString.value = _radiusAnalString.value + "\n\n세로로 홀쭉한 형태"
        }else if(_irisImage.value!!.rectWidth >(_irisImage.value!!.circleRadius * 2)
            && _irisImage.value!!.rectHeight < (_irisImage.value!!.circleRadius * 2)){
            _radiusAnalString.value = _radiusAnalString.value + "\n\n가로로 뚱뚱한 형태"
        }else{
            _radiusAnalString.value = _radiusAnalString.value + "\n\n고른 형태"
        }

        // 2> 중심 분석 Rect 중심점, 이미지로 표현
        val radiusCenterBitmap = autoSetPupilAndIris.drawRadius(bitmap,
            _irisImage.value!!.rectCenter, _irisImage.value!!.rectCenter,
            AppDataConstants.pupilParam1Color)

        val radiusBitmap = autoSetPupilAndIris.drawRadius(radiusCenterBitmap,
            _irisImage.value!!.rectStart, _irisImage.value!!.rectEnd,
            AppDataConstants.pupilParam1Color)

        // 3> 중심 분석 Circle 중심점, 이미지로 표현
        val circleCenterBitmap = autoSetPupilAndIris.drawRadius(radiusBitmap,
            _irisImage.value!!.circleCenter, _irisImage.value!!.circleCenter,
            AppDataConstants.pupilParam2Color)

        val circleBitmap = autoSetPupilAndIris.drawCircle(_irisImage.value!!.circleCenter, circleCenterBitmap,
            _irisImage.value!!.circleRadius,
            AppDataConstants.pupilParam2Color)

        _radiusAnalBitmap.value = circleBitmap
    }

    /**
     * 2. 중심 분석 : 동공을 둘러싼 직사각형의 중심 좌표와 동공 영역의 무게중심(1차 모멘텀)좌표를 비교 */
    fun getCenterAnal(bitmap: Bitmap){

        // 1> Rect 중심, Pupil 중심

        val distance = autoSetPupilAndIris.getDistance(_irisImage.value!!.rectCenter, _irisImage.value!!.circleCenter)
        val angle = autoSetPupilAndIris.getAngle(_irisImage.value!!.rectCenter, _irisImage.value!!.circleCenter)

        _centerAnalString.value = " rectCenter : (" + _irisImage.value!!.rectCenter.x + ", " + _irisImage.value!!.rectCenter.y + ")" +
                "\n pupilCenter : (" + _irisImage.value!!.circleCenter.x + ", " + _irisImage.value!!.circleCenter.y + ")" +
                "\n\n" +
                "두 점 사이의 길이  :  ${round(distance*10)/10} "+
                "\n" +
                "두 점 사이의 각도  :  ${round(angle*10)/10}"

        // 2> 중심 분석 Rect 중심점, 이미지로 표현
        val radiusCenterBitmap = autoSetPupilAndIris.drawRadius(bitmap,
            _irisImage.value!!.rectCenter, _irisImage.value!!.rectCenter,
            AppDataConstants.pupilParam1Color)

        val radiusBitmap = autoSetPupilAndIris.drawRadius(radiusCenterBitmap,
            _irisImage.value!!.rectStart, _irisImage.value!!.rectEnd,
            AppDataConstants.pupilParam1Color)

        // 3> 중심 분석 Circle 중심점, 이미지로 표현
        val circleCenterBitmap = autoSetPupilAndIris.drawRadius(radiusBitmap,
            _irisImage.value!!.circleCenter, _irisImage.value!!.circleCenter,
            AppDataConstants.pupilParam2Color)

        val circleBitmap = autoSetPupilAndIris.drawCircle(_irisImage.value!!.circleCenter,
            circleCenterBitmap,
            _irisImage.value!!.circleRadius,
            AppDataConstants.pupilParam2Color)

        _centerAnalBitmap.value = circleBitmap
    }

    /**
     * 3. Four Sector */
    fun getFourSectorAnal(bitmap: Bitmap) {

        // 각도 별로 동공 영역을 자른 후 해당하는 픽셀수를 표시
        var dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 0.0, 90.0)
        _fourSectorAnalBitmap.value = dst.first
        _fourSectorAnalString.value = " first(0 - 90) : " + dst.second

        dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 90.0, 180.0)
        _fourSectorAnalBitmap.value = dst.first
        _fourSectorAnalString.value = _fourSectorAnalString.value + "\n second(90 - 180) : " + dst.second

        dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 180.0, 270.0)
        _fourSectorAnalBitmap.value = dst.first
        _fourSectorAnalString.value = _fourSectorAnalString.value + "\n third(180 - 270) : " + dst.second

        dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 270.0, 360.0)
        _fourSectorAnalBitmap.value = dst.first
        _fourSectorAnalString.value = _fourSectorAnalString.value + "\n forth(270 - 360) : " + dst.second

    }

    /**
     * 4. Twelve Sector */
    fun getTwelveSectorAnal(bitmap: Bitmap){

        // 각도 별로 동공 영역을 자른 후 해당하는 픽셀수를 표시

        var dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 0.0, 30.0)
        _twelveAnalBitmap.value = dst.first
        _twelveAnalString.value = " (0 - 30) : " + dst.second

        dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 30.0, 60.0)
        _twelveAnalBitmap.value = dst.first
        _twelveAnalString.value = _twelveAnalString.value + "\n (30 - 60) : " + dst.second

        dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 60.0, 90.0)
        _twelveAnalBitmap.value = dst.first
        _twelveAnalString.value = _twelveAnalString.value + "\n (60 - 90) : " + dst.second

        dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 90.0, 120.0)
        _twelveAnalBitmap.value = dst.first
        _twelveAnalString.value = _twelveAnalString.value + "\n (90 - 120) : " + dst.second

        dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 120.0, 150.0)
        _twelveAnalBitmap.value = dst.first
        _twelveAnalString.value = _twelveAnalString.value + "\n (120 - 150) : " + dst.second

        dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 150.0, 180.0)
        _twelveAnalBitmap.value = dst.first
        _twelveAnalString.value = _twelveAnalString.value + "\n (150 - 180) : " + dst.second

        dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 180.0, 210.0)
        _twelveAnalBitmap.value = dst.first
        _twelveAnalString.value = _twelveAnalString.value + "\n (180 - 210) : " + dst.second

        dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 210.0, 240.0)
        _twelveAnalBitmap.value = dst.first
        _twelveAnalString.value = _twelveAnalString.value + "\n (210 - 240) : " + dst.second

        dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 240.0, 270.0)
        _twelveAnalBitmap.value = dst.first
        _twelveAnalString.value = _twelveAnalString.value + "\n (240 - 270) : " + dst.second

        dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 270.0, 300.0)
        _twelveAnalBitmap.value = dst.first
        _twelveAnalString.value = _twelveAnalString.value + "\n (270 - 300) : " + dst.second

        dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 300.0, 330.0)
        _twelveAnalBitmap.value = dst.first
        _twelveAnalString.value = _twelveAnalString.value + "\n (300 - 360) : " + dst.second

        dst = autoSetPupilAndIris.getArcCount(bitmap, _irisImage.value!!.circleCenter, 330.0, 360.0)
        _twelveAnalBitmap.value = dst.first
        _twelveAnalString.value = _twelveAnalString.value + "\n (330 - 360) : " + dst.second

    }

    /** 버튼 클릭 시 수행되는 함수들 */
    fun onClickedNextBtn(){
        _isClickedNextBtn.value = irisImage.value
    }


}