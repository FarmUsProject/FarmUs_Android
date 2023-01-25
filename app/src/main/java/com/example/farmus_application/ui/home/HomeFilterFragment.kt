package com.example.farmus_application.ui.home

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.farmus_application.databinding.FragmentHomeFilterBinding
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFilterFragment : Fragment() {

    private lateinit var homeFilterBinding : FragmentHomeFilterBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
    ): View? {
        // Inflate the layout for this fragment
        homeFilterBinding = FragmentHomeFilterBinding.inflate(inflater, container, false)

//      상단바 텍스트 설정
        homeFilterBinding.filterToolbar.toolbarMainTitleText.text = "필터"

        //툴바 back버튼 누르면 HomeFragment로 돌아가기
        homeFilterBinding.filterToolbar.toolbarWithTitleBackButton.setOnClickListener{
            requireActivity().run {
                startActivity(Intent(this, HomeFragment::class.java))
                finish()
            }
        }

//        DatePickerDialog 설정.
        homeFilterBinding.startDay.setOnClickListener {
            //datePickerDialog 에 표시할 달력
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                // y년 (m+1)월 d일
                homeFilterBinding.textStartDay.text = "${y}.${m + 1}.${d}."
            }

            //show를 통해 dialog창 활성화
            val picker = DatePickerDialog(requireContext(), listener, year, month, day)
            picker.show()

        }

        return homeFilterBinding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFilterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFilterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}