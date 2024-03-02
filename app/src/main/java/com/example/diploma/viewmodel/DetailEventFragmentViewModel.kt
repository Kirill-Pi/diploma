package com.example.diploma.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.example.diploma.App
import com.example.diploma.domain.Interactor
import java.net.URL
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DetailEventFragmentViewModel : ViewModel() {

    @Inject
    lateinit var interactor: Interactor

    init {
        App.instance.dagger.inject(this)
    }

    suspend fun loadWallpaper(url: String): Bitmap {
        return suspendCoroutine {
            val url = URL(url)
            println (url)
            val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            it.resume(bitmap)
        }
    }


}