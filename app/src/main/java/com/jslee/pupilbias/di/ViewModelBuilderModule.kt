package com.jslee.sdkmanager_kotlin.di

import androidx.lifecycle.ViewModelProvider
import com.jslee.pupilbias.util.compose.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelBuilderModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}