package com.jslee.pupilbias

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.jslee.pupilbias.databinding.ActivityMainBinding


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val FINISH_INTERVAL_TIME = 2000L
    private var backPressedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onBackPressed() {
        var tempTime:Long = System.currentTimeMillis();
        var intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            finish()
        } else {
            backPressedTime = tempTime
            super.onBackPressed()
        }
    }

//    override fun onResume() {
//        super.onResume()
//        if (!OpenCVLoader.initDebug()) {
//            Log.d(
//                "jjslee",
//                "Internal OpenCV library not found. Using OpenCV Manager for initialization"
//            )
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback)
//        } else {
//            Log.d("jjslee", "OpenCV library found inside package. Using it!")
//            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
//        }
//    }
//
//    private val mLoaderCallback: BaseLoaderCallback =
//        object : BaseLoaderCallback(this) {
//            override fun onManagerConnected(status: Int) {
//                when (status) {
//                    LoaderCallbackInterface.SUCCESS -> {
//                        Log.i(
//                            "jjslee",
//                            "OpenCV loaded successfully"
//                        )
//                    }
//                    else -> {
//                        super.onManagerConnected(status)
//                    }
//                }
//            }
//        }

//    override fun onResume() {
//        super.onResume()
//        if (!OpenCVLoader.initDebug()) {
//            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization")
//            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback)
//        } else {
//            Log.d("OpenCV", "OpenCV library found inside package. Using it!")
//            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
//        }
//    }
//
//    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
//        override fun onManagerConnected(status: Int) {
//            when (status) {
//                LoaderCallbackInterface.SUCCESS -> {
//                    Log.i("OpenCV", "OpenCV loaded successfully")
//                    //imageMat = Mat()
//                }
//                else -> {
//                    super.onManagerConnected(status)
//                }
//            }
//        }
//    }
}