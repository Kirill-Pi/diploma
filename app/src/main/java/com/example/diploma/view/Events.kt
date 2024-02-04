package com.example.diploma.view

import android.icu.util.Calendar
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.diploma.data.CalendarDateModel
import com.example.diploma.data.Launch
import com.example.diploma.view.rv_adapters.CalendarAdapter
import com.example.diploma.view.rv_adapters.HorizontalItemDecoration
import com.example.diploma.view.rv_adapters.LaunchListRecyclerAdapter
import com.example.diploma.view.rv_adapters.TopSpacingItemDecoration
import com.example.diploma.viewmodel.EventsViewModel
import com.example.pigolevmyapplication.R
import com.example.pigolevmyapplication.databinding.FragmentEventsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.Calendar.YEAR

class Events : Fragment() {

    companion object {
        fun newInstance() = Events()
    }

    private lateinit var binding: FragmentEventsBinding

    private val sdf = SimpleDateFormat(" LLLL yyyy", Locale.getDefault())
    private val cal = Calendar.getInstance(Locale.getDefault())
    private val currentDate = Calendar.getInstance(Locale.getDefault())
    private val dates = ArrayList<Date>()
    private lateinit var adapter: CalendarAdapter
    private val calendarList2 = ArrayList<CalendarDateModel>()
    private var now = Date()
    private var dateTxt = SimpleDateFormat("yyyy-MM-dd").format(now).toString()


    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(EventsViewModel::class.java)
    }

    private lateinit var launchesAdapter: LaunchListRecyclerAdapter
    private lateinit var scope: CoroutineScope
    private var launchDataBase = mutableListOf<Launch>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setUpClickListener()
        setUpCalendar()

        var scrollDate = currentDate.get(Calendar.DAY_OF_MONTH)
        if (scrollDate == 1) scrollDate++
        binding.recyclerView.scrollToPosition(scrollDate - 2)

        recyclerViewSetup()

        binding.homeRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var canScrollUp = false
            var canScrollDown = false
            var isLoaded = false

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
                   viewModel.nextPage(dateTxt)
                    scope = CoroutineScope(Dispatchers.IO).also { scope ->
                        scope.launch {
                            viewModel.launchListLiveData.collect {
                                withContext(Dispatchers.Main) {
                                    var list = it

                                    launchesAdapter.addPage(list)

                                    //launchDataBase = it
                                }
                            }
                        }
                    }

                }
            }
        })
    }


    private fun recyclerViewSetup() {
        binding.homeRecycler.apply {
            launchesAdapter =
                LaunchListRecyclerAdapter(object : LaunchListRecyclerAdapter.OnItemClickListener {

                    override fun click(news: Launch) {

                    }
                })
            println("adapter")
            println(launchDataBase)

            adapter = launchesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
            scope = CoroutineScope(Dispatchers.IO).also { scope ->
                scope.launch {
                    viewModel.launchListLiveData.collect {
                        withContext(Dispatchers.Main) {
                            var list = it

                            launchesAdapter.addItems(list)
                            launchDataBase = it
                        }
                    }
                }
            }
        }
    }

    /**
     * Set up click listener
     */
    private fun setUpClickListener() {
        binding.ivCalendarNext.setOnClickListener {
            cal.add(Calendar.MONTH, 1)
            setUpCalendar()
            binding.recyclerView.scrollToPosition(0)
        }
        binding.ivCalendarPrevious.setOnClickListener {
            cal.add(Calendar.MONTH, -1)
            if (cal == currentDate)
                setUpCalendar()
            else
                setUpCalendar()
            binding.recyclerView.scrollToPosition(30)
        }
    }

    /**
     * Setting up adapter for recyclerview
     */
    private fun setUpAdapter() {
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.single_calendar_margin)
        binding.recyclerView.addItemDecoration(HorizontalItemDecoration(spacingInPixels))
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerView)
        adapter = CalendarAdapter { calendarDateModel: CalendarDateModel, position: Int ->
            calendarList2.forEachIndexed { index, calendarModel ->
                calendarModel.isSelected = index == position
                if (calendarModel.isSelected) {
                    dateTxt = SimpleDateFormat("yyyy-MM-dd").format(calendarModel.data).toString()
                    println("date $dateTxt ")
                    viewModel.cleanDb()
                    viewModel.offsetSetup()
                    viewModel.getLaunches(dateTxt)

                    binding.recyclerView.scrollToPosition(index)

            }
        }
        adapter.setData(calendarList2)
    }
    binding.recyclerView.adapter = adapter
}




    /**
     * Function to setup calendar for every month
     */
    private fun setUpCalendar() {
        val calendarList = ArrayList<CalendarDateModel>()
        binding.tvDateMonth.text = sdf.format(cal.time).uppercase()
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)
        while (dates.size < maxDaysInMonth) {
            //println(monthCalendar.time)
            //println(currentDate.time)
            dates.add(monthCalendar.time)
            if (monthCalendar.time == currentDate.time) {
                calendarList.add(CalendarDateModel(monthCalendar.time, false, true))

            } else {
                calendarList.add(CalendarDateModel(monthCalendar.time))
            }
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        calendarList2.clear()
        calendarList2.addAll(calendarList)
        adapter.setData(calendarList)
    }



}