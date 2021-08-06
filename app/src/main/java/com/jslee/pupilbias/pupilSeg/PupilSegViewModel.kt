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
    private val _isClickedNextBtn = MutableLiveData<Boolean>()
    val isClickedNextBtn: LiveData<Boolean>
        get() = _isClickedNextBtn


    fun start (irisImage: IrisImage){
        _irisImage.value = irisImage
    }

    /**
     *  버튼 클릭 시 수행되는 함수들     */
    fun onClickedNextBtn(){
        _isClickedNextBtn.value = true
    }
}