package com.jslee.pupilbias.images

import androidx.lifecycle.ViewModel
import com.jslee.pupilbias.di.annotation.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ImagesModule {
    @Binds
    @IntoMap
    @ViewModelKey(ImagesViewModel::class)
    abstract fun bindViewModel(viewmodel: ImagesViewModel): ViewModel
}