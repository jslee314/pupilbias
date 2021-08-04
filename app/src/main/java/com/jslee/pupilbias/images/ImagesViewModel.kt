package com.jslee.pupilbias.images

import androidx.lifecycle.ViewModel
import com.jslee.pupilbias.data.AppRepository
import javax.inject.Inject

class ImagesViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {
}