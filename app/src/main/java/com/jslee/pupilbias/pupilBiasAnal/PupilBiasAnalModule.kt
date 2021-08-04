package com.jslee.pupilbias.pupilBiasAnal

import androidx.lifecycle.ViewModel
import com.jslee.pupilbias.di.annotation.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PupilBiasAnalModule {
    @Binds
    @IntoMap
    @ViewModelKey(PupilBiasAnalViewModel::class)
    abstract fun bindViewModel(viewmodel: PupilBiasAnalViewModel): ViewModel

}