package com.jslee.pupilbias.pupilSeg

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jslee.pupilbias.data.AppRepository
import com.jslee.pupilbias.data.vo.IrisImage
import javax.inject.Inject

class PupilSegViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _irisImage = MutableLiveData<IrisImage>()
    val irisImage: LiveData<IrisImage>
        get() = _irisImage

    /** 다음 버튼 클릭 여부*/
    private val _isClickedNextBtn = MutableLiveData<IrisImage>()
    val isClickedNextBtn: LiveData<IrisImage>
        get() = _isClickedNextBtn

    private val _isClickedSegPupilBtn = MutableLiveData<Boolean>()
    val isClickedSegPupilBtn: LiveData<Boolean>
        get() = _isClickedSegPupilBtn

    private val _isClickedCenterBtn = MutableLiveData<Boolean>()
    val isClickedCenterBtn: LiveData<Boolean>
        get() = _isClickedCenterBtn

    private val _isClickedCircleBtn = MutableLiveData<Boolean>()
    val isClickedCircleBtn: LiveData<Boolean>
        get() = _isClickedCircleBtn

    private val _isClickedMaskCenterBtn = MutableLiveData<Boolean>()
    val isClickedMaskCenterBtn: LiveData<Boolean>
        get() = _isClickedMaskCenterBtn

    private val _isClickedMaskCircleBtn = MutableLiveData<Boolean>()
    val isClickedMaskCircleBtn: LiveData<Boolean>
        get() = _isClickedMaskCircleBtn

    fun start (irisImage: IrisImage){
        _irisImage.value = irisImage

        _isClickedSegPupilBtn.value = false
        _isClickedNextBtn.value = null
        _isClickedCenterBtn.value = false
        _isClickedCircleBtn.value = false
        _isClickedMaskCenterBtn.value = false
        _isClickedMaskCircleBtn.value = false
    }

    /**
     *  버튼 클릭 시 수행되는 함수들     */
    fun onClickedSegPupilBtn(){
        _isClickedSegPupilBtn.value = true
    }

    fun onClickedCenterBtn(){
        _isClickedCenterBtn.value = true
    }

    fun onClickedCircleBtn(){
        _isClickedCircleBtn.value = true
    }

    fun onClickedMaskCenterBtn(){
        _isClickedMaskCenterBtn.value = true
    }

    fun onClickedMaskCircleBtn(){
        _isClickedMaskCircleBtn.value = true
    }

    fun onClickedNextBtn(){
        _isClickedNextBtn.value = irisImage.value
    }

    fun updateIrisImage(irisImage: IrisImage) {
        // LiveData 객체인 _irisImage 값을 변경해주어야 UI에 자동으로 변경된 값이 적용됨
        _irisImage.value = irisImage
    }
}