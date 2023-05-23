package com.example.farmus_application.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.farmus_application.databinding.FragmentTermsFourBinding



class TermsFourFragment: Fragment(){

    private lateinit var viewBinding : FragmentTermsFourBinding

    var TermsActivity: TermsActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        TermsActivity = context as TermsActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTermsFourBinding.inflate(inflater, container, false)

        // 클릭 시 프래그먼트 종료 후 메인 액티비티 다시 활성화
        viewBinding.temrsFourToolbar.toolbarDeleteButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            TermsActivity!!.activateMainLayout()
        }

        //클릭 시 다음 프래그먼트로 스왑
        viewBinding.temrsFourToolbar.toolbarNextButton.text = "완료"
        viewBinding.temrsFourToolbar.toolbarNextButton.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
            TermsActivity!!.activateMainLayout()
        }

        return viewBinding.root
    }
}