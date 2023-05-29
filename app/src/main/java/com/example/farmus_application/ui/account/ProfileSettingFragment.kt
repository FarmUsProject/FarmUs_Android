package com.example.farmus_application.ui.account

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.farmus_application.R
import com.example.farmus_application.databinding.FragmentProfileSettingBinding
import com.example.farmus_application.ui.MainActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileSettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileSettingFragment : Fragment() {

    private lateinit var binding: FragmentProfileSettingBinding

    //뒤로가기 기능 구현
    private lateinit var callback: OnBackPressedCallback

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
        binding = FragmentProfileSettingBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //BottomNavigationView 숨기기
        (activity as MainActivity).hideBottomNavigation(true)

        binding.btnProfileImageSetting.bringToFront()

        //toopbar 텍스트 설정
        binding.toolBar.toolbarMainTitleText.text = "프로필 수정"
        //툴바 백터튼 누르면 MyPageFragment로 이동
        binding.toolBar.toolbarWithTitleBackButton.setOnClickListener {
            (activity as MainActivity).changeFragment(MyPageFragment.newInstance("",""))
        }

        //todo 오류 해결
//        view.edittextIntroduction.addTextChangedListener(object : TextWatcher{
//
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                val count : String = p0?.length.toString()
//                view.edittextCount.text = "$count/20"
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//                val count : String = p0?.length.toString()
//                view.edittextCount.text = "$count/20"
//            }
//        })
    }

    //뒤로가기 누르면 MyPageFragment로 이동
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).changeFragment(MyPageFragment.newInstance("",""))
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    //뒤로가기 누르면 MyPageFragment로 이동
    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileSettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileSettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}