package com.example.diploma.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.diploma.viewmodel.SpaceShipsViewModel
import com.example.pigolevmyapplication.R

class SpaceShips : Fragment() {

    companion object {
        fun newInstance() = SpaceShips()
    }

    private lateinit var viewModel: SpaceShipsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_space_ships, container, false)
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(SpaceShipsViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}