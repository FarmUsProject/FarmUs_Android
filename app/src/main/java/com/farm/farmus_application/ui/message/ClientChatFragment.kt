package com.farm.farmus_application.ui.message

import android.annotation.SuppressLint
import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.farm.farmus_application.MyApplication
import com.farm.farmus_application.R
import com.farm.farmus_application.databinding.FragmentClientChatBinding
import com.farm.farmus_application.di.AppModule
import com.farm.farmus_application.repository.UserPrefsStorage
import com.farm.farmus_application.ui.MainActivity
import com.farm.farmus_application.ui.message.adapter.ClientChatRVAdapter
import com.farm.farmus_application.utilities.JWTUtils
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.Locale


class ClientChatFragment : Fragment() {
    private val TAG = "ClientChatFragmentTAG"
    private var _binding: FragmentClientChatBinding? = null
    private val binding get() = _binding!!
    private val jwtToken = UserPrefsStorage.accessToken
    private val name = JWTUtils.decoded(jwtToken.toString())?.tokenBody?.name ?: ""
    private var mSocket: Socket? = null
    private val onMessageReceived = Emitter.Listener { args ->
        Log.d(TAG, "update 이벤트 수신처리")
    }
    // TODO : 임시 데이터. 사용 예시
    private val chatMessages = ArrayList<ChatMessage>().apply {
        add(ChatMessage(name, "내가 보냄", "오후 2:16"))
        add(ChatMessage("kong", "상대방이 보냄", "오후 3:10"))
    }
    private lateinit var clientChatRVAdapter: ClientChatRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingSocket()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientChatBinding.inflate(inflater, container, false)
        mSocket?.connect()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNavigation(true)
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.hideBottomNavigation(false)
        _binding = null
    }

    private fun settingSocket() {
        try {
            mSocket = IO.socket("http://118.67.135.247:80")
            Log.d(TAG,"${mSocket?.id()}")
        } catch (e: URISyntaxException) {
            Log.d(TAG,"${e.message}")
        }
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                mSocket = IO.socket("https://www.farmus.kro.kr")
//                mSocket.connect()
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Log.d(TAG,"${e.message}")
//                }
//            }
//        }
//        mSocket.on("update", onMessageReceived)
    }

    private fun init() {
        initSendButton()
        initRecyclerView()
        initBackButton()
        initMoreButton()
        initBottomSheet()
        // TODO: 추가할 초기화가 있다면 초기화
    }

    private fun initSendButton() {

        binding.commonChatLayout.buttonSendMsg.setOnClickListener {
            // sendMessage()
            // 클라이언트에서 메시지를 서버로 전송
            val message = binding.commonChatLayout.edittextSendMsg.text.toString()
            val name = name
            val data = JSONObject().apply {
//                put("name", name)
//                put("message", message)
                put("type","message")
                put("message",message)
            }
            Log.d(TAG,"${mSocket?.connected()}")
//            mSocket.emit("send", data)
            clientChatRVAdapter.addMessage(ChatMessage(name,message,"00:00"))
            binding.commonChatLayout.edittextSendMsg.text.clear()
        }
    }

    private fun initRecyclerView() {
        // TODO : sender(현 사용자)의 id를 잘 전달해야함
        clientChatRVAdapter = ClientChatRVAdapter(chatMessages)
        binding.commonChatLayout.rvChat.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = clientChatRVAdapter

            // editText 가 눌리고 keyboard가 보이고 나서 recyclerview의 부분을 클릭하면 hide keyboard하기
            setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    v.performClick()
                    hideKeyboard()
                }
                false
            }

        }
    }

    private fun initBackButton() {
        binding.toolbarLayout.toolbarWithTitleAndMoreBackButton.setOnClickListener {
            hideKeyboard()

            // keyboard를 숨긴 후에 back되도록 (keyboard hide가 안된 것이 이전 화면에 영향을 줌)
            it.postDelayed({
                activity?.supportFragmentManager?.apply {
                    beginTransaction().remove(this@ClientChatFragment).commit()
                    popBackStack()
                }
            }, 100) // 100초 뒤에 처리됨
        }
    }

    private fun initMoreButton() {
        binding.toolbarLayout.toolbarWithTitleAndMoreMoreButton.setOnClickListener {
            val popup = PopupMenu(context, it)
            popup.menuInflater.inflate(R.menu.top_more_menu, popup.menu)
            // TODO: item click 리스너 구현 위치
            popup.show()
        }
    }

    private fun initBottomSheet() {
        binding.clientFarmInfo.clientChatHistory.setOnClickListener {
            val bottomSheetFragment = ChatBottomSheetFragment()
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    private fun settingTime() {
        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
        val timeFormat = SimpleDateFormat("aa hh:mm", Locale.KOREA)
        println(dateFormat.format(currentTime))
        println(timeFormat.format(currentTime))
    }

    override fun onDestroy() {
        super.onDestroy()
        // Socket 연결 해제
        mSocket?.disconnect()
    }

}