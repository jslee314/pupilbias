package com.jslee.pupilbias.pupilResult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jslee.pupilbias.data.AppRepository
import javax.inject.Inject

class PupilResultViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    /** 다음 버튼 클릭 여부*/
    private val _isClickedHomeBtn = MutableLiveData<Boolean>()
    val isClickedHomeBtn: LiveData<Boolean>
        get() = _isClickedHomeBtn

    /**
     *  버튼 클릭 시 수행되는 함수들     */
    fun onClickedHomeBtn(){
        _isClickedHomeBtn.value = true
    }

}