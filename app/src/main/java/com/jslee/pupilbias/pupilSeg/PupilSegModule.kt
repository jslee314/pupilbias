package com.jslee.pupilbias.pupilSeg

import androidx.lifecycle.ViewModel
import com.jslee.pupilbias.di.annotation.ViewModelKey
import com.jslee.pupilbias.pupilBiasAnal.PupilBiasAnalViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PupilSegModule {
    @Binds
    @IntoMap
    @ViewModelKey(PupilSegViewModel::class)
    abstract fun bindViewModel(viewmodel: PupilSegViewModel): ViewModel

}