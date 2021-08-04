package com.jslee.pupilbias.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class LocalDataSource constructor(private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AppDataSource {

}