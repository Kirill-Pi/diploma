package com.example.diploma.view

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.diploma.data.Events
import com.example.diploma.data.Launch
import com.example.diploma.notifications.NotificationHelper
import com.example.diploma.viewmodel.DetailEventFragmentViewModel
import com.example.diploma.viewmodel.DetailLaunchFragmentViewModel
import com.example.pigolevmyapplication.R
import com.example.pigolevmyapplication.databinding.FragmentDetailLaunchBinding
import com.example.pigolevmyapplication.databinding.FragmentDetailsEventBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class DetailEvents  : Fragment() {
    private lateinit var binding: FragmentDetailsEventBinding
    lateinit var event: Events
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DetailEventFragmentViewModel::class.java)
    }
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsEventBinding.inflate(inflater, container, false)
        val view = binding.root
        event = arguments?.get("event") as Events
        return view



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsInit()

        binding.detailsFabDownloadWp.setOnClickListener {
            performAsyncLoadOfPoster()
        }

        binding.detailsFab.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this Event: ${event.name} \n\n ${event.description}"
            )
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))
        }

        binding.detailsFabWatchLater.setOnClickListener {
            NotificationHelper.notificationSet(requireContext(), event)
        }

    }

    private fun detailsInit() {
        binding.detailsToolbar.title = event.name
        Glide.with(this)
            .load( event.image)
            .centerCrop()
            .into(binding.detailsPoster)
        binding.annotation = event.name
        binding.dateText.text = event.date
        binding.countryText.text = event.location
        binding.description.text = event.description
    }




    //Узнаем, было ли получено разрешение ранее
    private fun checkPermission(): Boolean {
//            println (checkPermission())
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        println (result == PackageManager.PERMISSION_GRANTED)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            1
        )
    }

    private fun saveToGallery(bitmap: Bitmap) {
        //Проверяем версию системы
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //Создаем объект для передачи данных
            val contentValues = ContentValues().apply {
                //Составляем информацию для файла (имя, тип, дата создания, куда сохранять и т.д.)
                put(MediaStore.Images.Media.TITLE, event.name)
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    event.name
                )
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.Images.Media.DATE_ADDED,
                    System.currentTimeMillis() / 1000
                )
                put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FilmsSearchApp")
            }
            //Получаем ссылку на объект Content resolver, который помогает передавать информацию из приложения вовне
            val contentResolver = requireActivity().contentResolver
            val uri = contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            //Открываем канал для записи на диск
            val outputStream = contentResolver.openOutputStream(uri!!)
            //Передаем нашу картинку, может сделать компрессию
            if (outputStream != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            //Закрываем поток
            outputStream?.close()
        } else {
            //То же, но для более старых версий ОС
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.insertImage(
                requireActivity().contentResolver,
                bitmap,
                event.name,
                event.description
            )
        }
    }

    private fun String.handleSingleQuote(): String {
        return this.replace("'", "")

    }


    private fun performAsyncLoadOfPoster() {

        println("Load")
        //Проверяем есть ли разрешение
//            if (!checkPermission()) {
//                //Если нет, то запрашиваем и выходим из метода
//                requestPermission()
//                return
//            }
        //Создаем родительский скоуп с диспатчером Main потока, так как будем взаимодействовать с UI
        MainScope().launch {
            println("main scope")
            //Включаем Прогресс-бар
            binding.progressBar.isVisible = true
            //Создаем через async, так как нам нужен результат от работы, то есть Bitmap
            val job = scope.async {
                println(event.image!!)
                viewModel.loadWallpaper( event.image!!)

            }
            //Сохраняем в галерею, как только файл загрузится
            saveToGallery(job.await())
            //Выводим снекбар с кнопкой перейти в галерею
            Snackbar.make(
                binding.root,
                R.string.downloaded_to_gallery,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.open) {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.type = "image/*"
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                .show()

            //Отключаем Прогресс-бар
            binding.progressBar.isVisible = false
        }
    }

}