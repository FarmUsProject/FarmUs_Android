package com.farm.farmus_application.ui.farm

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.farm.farmus_application.databinding.FragmentFarmTab1Binding
import com.farm.farmus_application.model.reserve.reserve_list.ReserveListResult
import com.farm.farmus_application.repository.UserPrefsStorage
import com.farm.farmus_application.ui.MainActivity
import com.farm.farmus_application.ui.farm.adapter.ReserveFarmListRVAdapter
import com.farm.farmus_application.ui.home.Adapter.EmptyDataObserve
import com.farm.farmus_application.utilities.JWTUtils
import com.farm.farmus_application.viewmodel.farm.FarmListViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReserveFarmListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ReserveFarmListFragment : Fragment() {

    private lateinit var binding: FragmentFarmTab1Binding
    private val farmListViewModel: FarmListViewModel by viewModels()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val jwtToken = UserPrefsStorage.accessToken
    private val email = JWTUtils.decoded(jwtToken.toString())?.tokenBody?.email?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFarmTab1Binding.inflate(layoutInflater, container, false)
        farmListViewModel.apply {
            getCurrentList()
            getPastList()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecentRecyclerView()
        initPastRecyclerView()

    }

    private fun initRecentRecyclerView() {
        val recentAdapter = ReserveFarmListRVAdapter().apply {
            setOnClickListener(object : ReserveFarmListRVAdapter.OnClickListener {
                override fun onClick(view: View, data: ReserveListResult, pos: Int) {
                    moveToFarmDetail(data.FarmID)
                }
            })
        }

        binding.rvRecent.apply {
            adapter = recentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val emptyDataObserve = EmptyDataObserve(binding.rvRecent, binding.emptyCurrentDataParent.root)
        recentAdapter.registerAdapterDataObserver(emptyDataObserve)

        farmListViewModel.currentFarmList.observe(viewLifecycleOwner) {
            Log.d("CurrentFarmList:",it.toString())
            recentAdapter.submitList(it.result)
        }

    }

    private fun initPastRecyclerView() {
        val pastAdapter = ReserveFarmListRVAdapter().apply {
            setOnClickListener(object : ReserveFarmListRVAdapter.OnClickListener {
                override fun onClick(view: View, data: ReserveListResult, pos: Int) {
                    moveToFarmDetail(data.FarmID)
                }
            })
        }
        binding.rvPast.apply {
            adapter = pastAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        val emptyDataObserve = EmptyDataObserve(binding.rvPast, binding.emptyPastDataParent.root)
        pastAdapter.registerAdapterDataObserver(emptyDataObserve)

        farmListViewModel.pastFarmList.observe(viewLifecycleOwner) {
            pastAdapter.submitList(it.result)
        }
    }

    private fun moveToFarmDetail(farmId: Int) {
        val farmDetailFragment = FarmDetailFragment()
        val bundle = Bundle().apply {
            putInt("farmId",farmId)
        }
        farmDetailFragment.arguments = bundle
        (activity as MainActivity).changeFragmentAddToBackStack(farmDetailFragment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Tab1Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReserveFarmListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}