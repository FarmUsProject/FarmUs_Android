package com.farm.farmus_application.ui.message.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.farm.farmus_application.databinding.ItemContainerReceivedMessageBinding
import com.farm.farmus_application.databinding.ItemContainerSentMessageBinding
import com.farm.farmus_application.repository.UserPrefsStorage
import com.farm.farmus_application.ui.message.ChatMessage
import com.farm.farmus_application.utilities.JWTUtils


// TODO : chatMessages List를 어떻게 받는 가에 따라서 파라미터로 처리가 안될 수도 있음
// TODO : 구현시 나의 채팅 상대방 프로필 이미지를 끌어오는 방법이 필요함
class ClientChatRVAdapter(private val chatMessages: ArrayList<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val jwtToken = UserPrefsStorage.accessToken
    private val name = JWTUtils.decoded(jwtToken.toString())?.tokenBody?.name ?: ""

    enum class ViewType {
        VIEW_TYPE_SENT, VIEW_TYPE_RECEIVED,
//        VIEW_TYPE_DAY
    }


    override fun getItemViewType(position: Int): Int {
        val chatMessage = chatMessages[position]
//        return when {
//            // name == email 과 같다면 내가 보낸 메시지
//            // 상대방이 보낸 메시지는 name != email로 구분가능하지만
//            // 날짜가 바뀌는 날의 구분 기준을 뭐로 잡지..
//            chatMessage.name == name -> {
//                ViewType.VIEW_TYPE_SENT.ordinal
//            }
//            chatMessage.senderId == "0" && chatMessage.receiverId == "0" -> {
//                ViewType.VIEW_TYPE_DAY.ordinal
//            }
//            else -> {
//                ViewType.VIEW_TYPE_RECEIVED.ordinal
//            }
//        }
        // 내가 보낸것과 상대방이 보낸것은 구분이 가능, 그러나 날짜가 바뀌었을 경우는 어떻게 View를 지정해야 할지 (날짜가 같이 나오는 itemView는 이미 만들어놓음)
        // 전달받은 메세지 List의 마지막 index의 datetime과 비교하여 다르다면, 날짜가 바뀌는 View를 보여주는?
        return when (chatMessage.name) {
            name -> {
                ViewType.VIEW_TYPE_SENT.ordinal
            }
            else -> {
                ViewType.VIEW_TYPE_RECEIVED.ordinal
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (ViewType.values()[viewType]) {
            ViewType.VIEW_TYPE_SENT -> {
                val binding = ItemContainerSentMessageBinding.inflate(layoutInflater, parent, false)
                SentMessageViewHolder(binding)
            }
            ViewType.VIEW_TYPE_RECEIVED -> {
                val binding = ItemContainerReceivedMessageBinding.inflate(layoutInflater, parent, false)
                ReceivedMessageViewHolder(binding)
            }
//            ViewType.VIEW_TYPE_DAY -> {
//                val binding = ItemContainerDateBinding.inflate(layoutInflater, parent, false)
//                DayViewHolder(binding)
//            }
        }
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatMessage = chatMessages[position]
        when (ViewType.values()[getItemViewType(position)]) {
            ViewType.VIEW_TYPE_SENT -> (holder as SentMessageViewHolder).setData(chatMessage)
            ViewType.VIEW_TYPE_RECEIVED -> (holder as ReceivedMessageViewHolder).setData(chatMessage)
//            ViewType.VIEW_TYPE_DAY -> (holder as DayViewHolder).setData(chatMessage)
        }
    }

    fun addMessage(item: ChatMessage) {
        chatMessages.add(item)
        notifyDataSetChanged()
    }

    class SentMessageViewHolder(private val binding: ItemContainerSentMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(chatMessage: ChatMessage) {
//            binding.textMessage.text = chatMessage.message
//            binding.textDateTime.text = chatMessage.dateTime
            binding.chatModel = chatMessage
        }
    }

    class ReceivedMessageViewHolder(private val binding: ItemContainerReceivedMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(chatMessage: ChatMessage) {
//            binding.textMessage.text = chatMessage.message
//            binding.textDateTime.text = chatMessage.dateTime
            binding.chatModel = chatMessage
        }
    }
//    class DayViewHolder(private val binding: ItemContainerDateBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun setData(chatMessage: ChatMessage) {
//            binding.tvDate.text = chatMessage.message
//        }
//    }
}