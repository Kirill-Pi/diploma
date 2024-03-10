package com.example.diploma.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diploma.data.Favorites
import com.example.diploma.view.rv_adapters.FavoritesListRecyclerAdapter
import com.example.diploma.view.rv_adapters.TopSpacingItemDecoration
import com.example.diploma.viewmodel.FavoritesViewModel
import com.example.pigolevmyapplication.databinding.FragmentFavoritesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(FavoritesViewModel::class.java)
    }
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoritesAdapter: FavoritesListRecyclerAdapter
    private lateinit var scope: CoroutineScope
    private var favoritesDataBase = mutableListOf<Favorites>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.interactor.getFavoritesFromDB()
        recyclerViewSetup()
        searchViewInit(binding)
    }

    private fun recyclerViewSetup() {
        binding.mainRecycler.apply {
            favoritesAdapter =
                FavoritesListRecyclerAdapter(object :
                    FavoritesListRecyclerAdapter.OnItemClickListener {
                    override fun click(spaceCraft: Favorites) {
                        (requireActivity() as MainActivity).launchDetailsFavoritesFragment(
                            spaceCraft
                        )
                    }
                })
            adapter = favoritesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
            scope = CoroutineScope(Dispatchers.IO).also { scope ->
                scope.launch {
                    viewModel.favoritesListLiveData.collect {
                        withContext(Dispatchers.Main) {
                            var list = it
                            favoritesAdapter.addItems(list)
                            favoritesDataBase = it
                        }
                    }
                }
            }
        }
    }

    private fun searchViewInit(binding: FragmentFavoritesBinding) {
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isEmpty() == true) {
                    favoritesAdapter.addItems(favoritesDataBase)
                    return true
                }
                val result = favoritesDataBase.filter {

                    it.name.lowercase(Locale.getDefault()).contains(
                        newText?.lowercase(Locale.getDefault())!!
                    )
                }
                favoritesAdapter.addItems(result.toMutableList())
                return true
            }
        })
    }
}
