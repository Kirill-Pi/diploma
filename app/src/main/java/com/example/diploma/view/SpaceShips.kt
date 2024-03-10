package com.example.diploma.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diploma.data.RecentlySeen
import com.example.diploma.data.SpacecraftConfig
import com.example.diploma.view.rv_adapters.LastSeenListRecyclerAdapter
import com.example.diploma.view.rv_adapters.SpaceShipsListRecyclerAdapter
import com.example.diploma.view.rv_adapters.TopSpacingItemDecoration
import com.example.diploma.viewmodel.SpaceShipsViewModel
import com.example.pigolevmyapplication.databinding.FragmentSpaceShipsBinding
import kotlinx.coroutines.*
import java.util.*

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
    private var lastSeenDataBase = mutableListOf<RecentlySeen>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSpaceShipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewSetup()
        searchViewInit(binding)
        bottomSheetSetup()
        scrollListenerSetup()
        cleanButtonSetup()
    }

    private fun cleanButtonSetup() {
        binding.bottomView.clear.setOnClickListener {
            viewModel.interactor.cleanRecentlySeenDb()
        }
    }

    private fun scrollListenerSetup() {
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

    private fun bottomSheetSetup() {
        binding.bottomView.bottomRecycler.apply {
            recentlyViewedAdapter =
                LastSeenListRecyclerAdapter(object :
                    LastSeenListRecyclerAdapter.OnItemClickListener {
                    override fun click(spaceCraft: RecentlySeen) {
                    }
                })
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
            adapter = spaceCraftsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
            scope = CoroutineScope(Dispatchers.IO).also { scope ->
                scope.launch {
                    viewModel.spaceCraftsListLiveData.collect {
                        withContext(Dispatchers.Main) {
                            var list = it
                            list.map {
                                it.isInFavorites = viewModel.interactor.checkFavoriteByName(it.name)
                            }
                            spaceCraftsAdapter.addItems(list)
                            spaceCraftDataBase = it
                        }
                    }
                }
            }
        }
    }

    private fun searchViewInit(binding: FragmentSpaceShipsBinding) {
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isEmpty() == true) {
                    return true
                }
                val query = newText?.lowercase(Locale.getDefault())!!
                viewModel.searchQuerySetUp(query)
                viewModel.offsetSetup()
                viewModel.getSpacecrafts()
                return true
            }
        })
    }
    override fun onStop() {
        viewModel.searchQuerySetUp("")
        super.onStop()
        scope.cancel()
    }

}