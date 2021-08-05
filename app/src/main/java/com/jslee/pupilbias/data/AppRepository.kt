package com.jslee.pupilbias.data

import android.content.Context
import com.jslee.pupilbias.di.annotation.LocalDataSourceAnnotation
import com.jslee.pupilbias.di.annotation.RemoteDataSourceAnnotation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class AppRepository@Inject constructor(
    @RemoteDataSourceAnnotation private var remoteDataSource: AppDataSource,
    @LocalDataSourceAnnotation private var localDataSource: AppDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val context: Context) {

}