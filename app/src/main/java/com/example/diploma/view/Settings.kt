package com.example.diploma.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.diploma.viewmodel.SettingsViewModel
import com.example.pigolevmyapplication.R
import com.example.pigolevmyapplication.databinding.FragmentSettingsBinding

class Settings : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SettingsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.inUsePropertyLifeData.observe(viewLifecycleOwner, Observer<Int> {
            when(it) {
                1 -> binding.radioGroup2.check(R.id.in_use_1)
                2 -> binding.radioGroup2.check(R.id.in_use_2)
                3 -> binding.radioGroup2.check(R.id.in_use_3)
            }
        })
        //Слушатель для отправки нового состояния в настройки
        binding.radioGroup2.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.in_use_1 -> viewModel.putIsInUseProperty(1)
                R.id.in_use_2 -> viewModel.putIsInUseProperty(2)
                R.id.in_use_3 -> viewModel.putIsInUseProperty(3)
            }
        }

        viewModel.mannedPropertyLifeData.observe(viewLifecycleOwner, Observer<Int> {
            when(it) {
                1 -> binding.radioGroup1.check(R.id.human_rated_1)
                2 -> binding.radioGroup1.check(R.id.human_rated_2)
                3 -> binding.radioGroup1.check(R.id.human_rated_3)
            }
        })
        //Слушатель для отправки нового состояния в настройки
        binding.radioGroup1.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.human_rated_1 -> viewModel.putIsMannedProperty(1)
                R.id.human_rated_2 -> viewModel.putIsMannedProperty(2)
                R.id.human_rated_3 -> viewModel.putIsMannedProperty(3)

            }
        }
    }
}