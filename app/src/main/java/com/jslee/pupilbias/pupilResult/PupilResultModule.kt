package com.jslee.pupilbias.pupilResult

import androidx.lifecycle.ViewModel
import com.jslee.pupilbias.di.annotation.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PupilResultModule {
    @Binds
    @IntoMap
    @ViewModelKey(PupilResultViewModel::class)
    abstract fun bindViewModel(viewmodel: PupilResultViewModel): ViewModel

}