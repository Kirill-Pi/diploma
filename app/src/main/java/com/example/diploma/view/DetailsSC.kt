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
import com.example.diploma.data.SpacecraftConfig
import com.example.diploma.viewmodel.DetailsSCFragmentViewModel
import com.example.pigolevmyapplication.R
import com.example.pigolevmyapplication.databinding.FragmentDetailsScBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class DetailsSC  : Fragment() {
    private lateinit var binding: FragmentDetailsScBinding
    lateinit var spaceCraft: SpacecraftConfig
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(DetailsSCFragmentViewModel::class.java)
    }
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsScBinding.inflate(inflater, container, false)
        val view = binding.root
        spaceCraft = arguments?.get("spaceCraft") as SpacecraftConfig
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsInit()
        favoritesInit()
        viewModel.interactor.updateRecentlySeenDB(spaceCraft)
        binding.detailsFabDownloadWp.setOnClickListener {
            performAsyncLoadOfPoster()
        }

        binding.detailsFab.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out this Space Craft: ${spaceCraft.name} \n\n ${spaceCraft.capability}"
            )
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))
        }

    }

    private fun detailsInit() {
        binding.detailsToolbar.title = spaceCraft.name
        Glide.with(this)
            .load(spaceCraft.imageUrl)
            .placeholder(R.drawable.launch_placeholder)
            .error(R.drawable.launch_placeholder)
            .centerCrop()
            .into(binding.detailsPoster)
        binding.annotation = spaceCraft.capability
        if (spaceCraft.crewCapacity > 0) {
            binding.crewText.text = spaceCraft.crewCapacity.toString()
        } else binding.crewText.text = "unmanned"
        binding.countryText.text = spaceCraft.countryCode
        binding.maidenFlightText.text = spaceCraft.maidenFlight
        binding.capability.text = spaceCraft.capability
        if (spaceCraft.inUse) binding.inUseText.text = "Yes"
        else binding.inUseText.text = "No"
    }


    private fun favoritesInit() {
        binding.detailsFabFavorites.setImageResource(
            if (spaceCraft.isInFavorites) R.drawable.baseline_favorite_24
            else R.drawable.baseline_favorite_border_24
        )
        binding.detailsFabFavorites.setOnClickListener {
            if (!spaceCraft.isInFavorites) {
                binding.detailsFabFavorites.setImageResource(R.drawable.baseline_favorite_24)
                spaceCraft.isInFavorites = true
                viewModel.interactor.updateFavoritesDB(spaceCraft)
            } else {
                binding.detailsFabFavorites.setImageResource(R.drawable.baseline_favorite_border_24)
                spaceCraft.isInFavorites = false
                viewModel.interactor.deleteFavoriteItem(spaceCraft.name)
            }
        }

    }

    //Узнаем, было ли получено разрешение ранее
    private fun checkPermission(): Boolean {
//            println (checkPermission())
        val result = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        println(result == PackageManager.PERMISSION_GRANTED)
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
                put(MediaStore.Images.Media.TITLE, spaceCraft.name)
                put(
                    MediaStore.Images.Media.DISPLAY_NAME,
                    spaceCraft.name
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
                spaceCraft.name,
                spaceCraft.capability
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
                println(spaceCraft.imageUrl!!)
                viewModel.loadWallpaper(spaceCraft.imageUrl!!)

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