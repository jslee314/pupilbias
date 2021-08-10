package com.jslee.pupilbias.images

import android.content.res.AssetManager
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jslee.pupilbias.data.AppRepository
import com.jslee.pupilbias.data.vo.IrisImage
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject


class ImagesViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _irisImages = MutableLiveData<MutableList<IrisImage>>()
    val irisImages: LiveData<MutableList<IrisImage>>
        get() = _irisImages

    /** 다음 버튼 클릭 여부*/
    private val _isClickedNextBtn = MutableLiveData<IrisImage>()
    val isClickedNextBtn: LiveData<IrisImage>
        get() = _isClickedNextBtn

    fun start(assetManager: AssetManager){
        _irisImages.value = mutableListOf<IrisImage>()
        setIrisImage(assetManager)
    }

    private fun setIrisImage(assetManager: AssetManager){

        try {
            var inputStream: InputStream
            var files = assetManager.list("iris")
            for(i in files!!.indices) {
                inputStream = assetManager.open("iris/" + files[i])
                val drawable: Drawable = Drawable.createFromStream(inputStream, null)
                val irisImg: IrisImage = IrisImage(id = i ,fileName = files[i],  originalImg = drawable)
                _irisImages.value?.add(irisImg)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     *  버튼 클릭 시 수행되는 함수들     */
    /*** RecyclerView의 하나의 아이템을 클릭하면
     * [_navigateToSelectedProperty] [MutableLiveData]를 설정한다.
     * [groundProperty]:  클릭 된 GroundProperty */
    fun onClickedNextBtn(irisImage: IrisImage) {
        _isClickedNextBtn.value = irisImage
    }

    /**
     * Navigation이 수행 되면 [_navigateToSelectedProperty] 를 null로 설정      */
    fun displayPropertyDetailsComplete() {
        _isClickedNextBtn.value = null
    }


}