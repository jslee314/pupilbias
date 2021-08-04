package com.jslee.pupilbias.home

import androidx.lifecycle.ViewModel
import com.jslee.pupilbias.data.AppRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {
}