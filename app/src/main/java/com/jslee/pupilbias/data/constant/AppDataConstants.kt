package com.jslee.pupilbias.data.constant

import org.opencv.core.Scalar

class AppDataConstants {

    companion object{
        val Parm_R = 6372.8 * 1000

        val pupilCenterColor = Scalar(255.0, 255.0, 0.0)
        val pupilCircleColor = Scalar(255.0, 0.0, 0.0)
        val pupilRectColor = Scalar(0.0, 0.0, 255.0)

        val pupilParam1Color = Scalar(0.0, 255.0, 255.0)
        val pupilParam2Color = Scalar(255.0, 255.0, 0.0)
        val pupilParam3Color = Scalar(255.0, 0.0, 255.0, 200.0)

    }


}