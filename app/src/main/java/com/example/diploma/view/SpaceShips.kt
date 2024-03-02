package com.example.diploma.view

import android.app.ProgressDialog.show
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.diploma.data.CalendarDateModel
import com.example.diploma.data.Launch
import com.example.diploma.data.SpacecraftConfig
import com.example.diploma.view.rv_adapters.*
import com.example.diploma.viewmodel.EventsViewModel
import com.example.diploma.viewmodel.SpaceShipsViewModel
import com.example.pigolevmyapplication.R
import com.example.pigolevmyapplication.databinding.FragmentEventsBinding
import com.example.pigolevmyapplication.databinding.FragmentSpaceShipsBinding
import com.example.pigolevmyapplication.databinding.FragmentSpaceShipsBindingImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

class SpaceShips : Fragment() {

    companion object {
        fun newInstance() = SpaceShips()
    }



    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SpaceShipsViewModel::class.java)
    }
    private lateinit var binding: FragmentSpaceShipsBinding

    private lateinit var spaceCraftsAdapter: SpaceShipsListRecyclerAdapter
    private lateinit var recentlyViewedAdapter: LastSeenListRecyclerAdapter
    private lateinit var scope: CoroutineScope
    private var spaceCraftDataBase = mutableListOf<SpacecraftConfig>()
    private var lastSeenDataBase = mutableListOf<SpacecraftConfig>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpaceShipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // viewModel.getSpacecrafts()
        recyclerViewSetup()

        binding.bottomView.bottomRecycler.apply {
            recentlyViewedAdapter =
                LastSeenListRecyclerAdapter(object :
                    LastSeenListRecyclerAdapter.OnItemClickListener {

                    override fun click(spaceCraft: SpacecraftConfig) {
                        (requireActivity() as MainActivity).launchDetailsSCFragment(spaceCraft)
                    }
                })
            // println("adapter")
            //println(spaceCraftDataBase)

            adapter = recentlyViewedAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val decorator = TopSpacingItemDecoration(4)
            addItemDecoration(decorator)
            scope = CoroutineScope(Dispatchers.IO).also { scope ->
                scope.launch {
                    viewModel.lastSeenListLiveData.collect {
                        withContext(Dispatchers.Main) {
                            var list = it

                            recentlyViewedAdapter.addItems(list)
                            lastSeenDataBase = it
                        }
                    }
                }
            }
        }



        binding.mainRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val tempLayoutManager = binding.mainRecycler.layoutManager as LinearLayoutManager
                val visibleItemCount: Int = tempLayoutManager.childCount
                val totalItemCount: Int = tempLayoutManager.itemCount
                val firstVisibleItemPosition: Int = tempLayoutManager.findFirstVisibleItemPosition()
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    //Вызываем загрузку следующей страницы при достижении конца списка
                    && firstVisibleItemPosition >= 0
                ) {

                    viewModel.nextPage()
                    scope = CoroutineScope(Dispatchers.IO).also { scope ->
                        scope.launch {
                            viewModel.spaceCraftsListLiveData.collect {
                                withContext(Dispatchers.Main) {
                                    var list = it

                                    spaceCraftsAdapter.addPage(list)

                                    spaceCraftDataBase = it
                                }
                            }
                        }
                    }

                }
            }
        })

    }

    private fun recyclerViewSetup() {
        binding.mainRecycler.apply {
            spaceCraftsAdapter =
                SpaceShipsListRecyclerAdapter(object :
                    SpaceShipsListRecyclerAdapter.OnItemClickListener {

                    override fun click(spaceCraft: SpacecraftConfig) {
                        (requireActivity() as MainActivity).launchDetailsSCFragment(spaceCraft)
                    }
                })
            println("adapter")
            println(spaceCraftDataBase)

            adapter = spaceCraftsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
            scope = CoroutineScope(Dispatchers.IO).also { scope ->
                scope.launch {
                    viewModel.spaceCraftsListLiveData.collect {
                        withContext(Dispatchers.Main) {
                            var list = it

                            spaceCraftsAdapter.addItems(list)
                            spaceCraftDataBase = it
                        }
                    }
                }
            }
        }
    }

}