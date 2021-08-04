package com.jslee.pupilbias.pupilResult

import androidx.lifecycle.ViewModel
import com.jslee.pupilbias.data.AppRepository
import javax.inject.Inject

class PupilResultViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {
}