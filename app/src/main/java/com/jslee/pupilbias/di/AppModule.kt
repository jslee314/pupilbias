package com.jslee.pupilbias.di

import android.content.Context
import com.jslee.pupilbias.MyApplication
import com.jslee.pupilbias.data.AppDataSource
import com.jslee.pupilbias.data.LocalDataSource
import com.jslee.pupilbias.data.RemoteDataSource
import com.jslee.pupilbias.di.annotation.LocalDataSourceAnnotation
import com.jslee.pupilbias.di.annotation.RemoteDataSourceAnnotation
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


/**
 * @Module 어노태이션을 붙인 AppModule 클래스는 App을 만들기 위해 필요한 모든 객체를 얻을 수 있다.
 * 타 클래스에 의존성이 있는 클래스들을 제공해주는(provide~) 클래스를 정의해주는데 쓰임
 *
 * @Provides 어노테이션을 붙인 각 함수를 Component 클래스에게 제공해줌
 *
 * Component에 연결되어 의존성 객체를 생성하는 역할입니다. 생성 후 Scope에 따라 객체를 관리도 합니다
 * @Module은 클래스에만 붙이고, @Provides는 반드시 @Module 클래스에 선언된 메서드에만 사용합니다
 * Module 클래스는 의존성 주입에 필요한 객체들을 @Provide @Binds 메서드를 통해 관리합니다.
 * @Provides @Binds메서드의 파라미터 또한 Component에게 객체를 전달받고 Component에게 제공합니다
 */
@Module
object AppModule {

    /** 필요한 인자(AppDatabase, CoroutineDispatcher)를 Component로 부터 전달받아  RemoteDataSource 객체를 생성해서 Component에게 제공  */
    @JvmStatic
    @Singleton
    @RemoteDataSourceAnnotation
    @Provides
    fun provideRemoteDataSource(
        ioDispatcher: CoroutineDispatcher
    ): AppDataSource {
        return RemoteDataSource(ioDispatcher)
    }

    /** 필요한 인자(AppDatabase, CoroutineDispatcher)를 Component로 부터 전달받아 LocalDataSource 객체를 생성해서 Component에게 제공  */
    @JvmStatic
    @Singleton
    @LocalDataSourceAnnotation
    @Provides
    fun provideLocalDataSource(
        ioDispatcher: CoroutineDispatcher
    ): AppDataSource {
        return LocalDataSource(ioDispatcher)
    }

    /** CoroutineDispatcher 객체(인스턴스)를 생성해서 Component에게 제공  */
    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}