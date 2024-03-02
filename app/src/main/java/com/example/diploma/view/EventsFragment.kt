package com.example.diploma.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.diploma.data.Events
import com.example.diploma.view.rv_adapters.EventsListRecyclerAdapter
import com.example.diploma.view.rv_adapters.TopSpacingItemDecoration
import com.example.diploma.viewmodel.EventsViewModel
import com.example.pigolevmyapplication.databinding.FragmentEventsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventsFragment : Fragment() {

    companion object {
        fun newInstance() = EventsFragment()
    }


    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(EventsViewModel::class.java)
    }
    private lateinit var binding: FragmentEventsBinding
    private lateinit var scope: CoroutineScope
    private lateinit var eventsAdapter: EventsListRecyclerAdapter


    private var eventsDataBase: MutableList<Events> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getEvents()
        println ("events $eventsDataBase")
        recyclerViewSetup()



        binding.homeRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val tempLayoutManager = binding.homeRecycler.layoutManager as LinearLayoutManager
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
                            viewModel.eventsListLiveData.collect {
                                withContext(Dispatchers.Main) {
                                    var list = it
                                    eventsAdapter.addPage(list)
                                    eventsDataBase = it
                                }
                            }
                        }
                    }
                }
            }
        })

    }

    private fun recyclerViewSetup() {
        binding.homeRecycler
            .apply {
                eventsAdapter =
                    EventsListRecyclerAdapter(object : EventsListRecyclerAdapter.OnItemClickListener {
                        override fun click(event: Events) {
                            println ("detail $event")
                            (requireActivity() as MainActivity).launchDetailsEventFragment(event)
                        }
                    })
                eventsAdapter.addItems(eventsDataBase)

            adapter = eventsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
                scope = CoroutineScope(Dispatchers.IO).also { scope ->
                    scope.launch {
                        viewModel.eventsListLiveData.collect {
                            withContext(Dispatchers.Main) {
                                var list = it
                                eventsAdapter.addItems(list)
                                eventsDataBase = it
                            }
                        }
                    }
                }
        }
    }
}



