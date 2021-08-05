package tensorflowlite.data

import android.graphics.Bitmap

data class ModelExecutionResultVO(
    var bitmapResult: Bitmap, // 원본이랑 마스크를 합친 이미지
    var bitmapOriginal: Bitmap, // 원본만
    var bitmapMaskOnly: Bitmap, // 마스크 그레이스케일
    var executionLog: String,
    var itemsFound: Set<Int>
)
