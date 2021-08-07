package com.jslee.pupilbias

import android.app.Application
import com.jslee.pupilbias.di.AppComponent
import com.jslee.pupilbias.di.DaggerAppComponent
import org.opencv.android.OpenCVLoader


class MyApplication: Application() {

    companion object {
        init {
            OpenCVLoader.initDebug()
        }
    }

    // 프로젝트의 모든 활동에서 사용할 AppComponent 인스턴스
    val appComponent: AppComponent by lazy {
        initializeComponent()

    }

    fun initializeComponent(): AppComponent {
        // create() 함수로 Component 구현
        // Factory 생성자를 사용하여 AppComponent의 인스턴스를 만듦
        // 그래프에서 Context로 사용될 applicationContext를 전달
        return DaggerAppComponent.factory().create(applicationContext)

    }
    override fun onCreate() {
        super.onCreate()
    }
}