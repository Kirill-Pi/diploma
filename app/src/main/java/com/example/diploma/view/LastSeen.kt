package com.example.diploma.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diploma.data.SpacecraftConfig
import com.example.diploma.view.rv_adapters.LastSeenListRecyclerAdapter
import com.example.diploma.view.rv_adapters.SpaceShipsListRecyclerAdapter
import com.example.diploma.view.rv_adapters.TopSpacingItemDecoration
import com.example.diploma.viewmodel.LastSeenViewModel
import com.example.diploma.viewmodel.SpaceShipsViewModel
import com.example.pigolevmyapplication.databinding.FragmentSpaceShipsBinding

import com.example.pigolevmyapplication.databinding.LastSeenFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LastSeen : Fragment() {



    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(LastSeenViewModel::class.java)
    }
    private lateinit var binding: LastSeenFragmentBinding

    private lateinit var spaceCraftsAdapter: LastSeenListRecyclerAdapter
    private lateinit var scope: CoroutineScope
    private var lastSeenDataBase = mutableListOf<SpacecraftConfig>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LastSeenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // viewModel.lastSeenListLiveData
        recyclerViewSetup()
    }

    private fun recyclerViewSetup() {
        binding.bottomRecycler.apply {
            spaceCraftsAdapter =
                LastSeenListRecyclerAdapter(object :
                    LastSeenListRecyclerAdapter.OnItemClickListener {

                    override fun click(spaceCraft: SpacecraftConfig) {
                        (requireActivity() as MainActivity).launchDetailsSCFragment(spaceCraft)
                    }
                })
           // println("adapter")
            //println(spaceCraftDataBase)

            adapter = spaceCraftsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val decorator = TopSpacingItemDecoration(4)
            addItemDecoration(decorator)
            scope = CoroutineScope(Dispatchers.IO).also { scope ->
                scope.launch {
                    viewModel.lastSeenListLiveData.collect {
                        withContext(Dispatchers.Main) {
                            var list = it

                            spaceCraftsAdapter.addItems(list)
                            lastSeenDataBase = it
                        }
                    }
                }
            }
        }
    }

}