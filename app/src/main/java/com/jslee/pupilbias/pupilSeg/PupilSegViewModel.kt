package com.jslee.pupilbias.pupilSeg

import androidx.lifecycle.ViewModel
import com.jslee.pupilbias.data.AppRepository
import javax.inject.Inject

class PupilSegViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {
}