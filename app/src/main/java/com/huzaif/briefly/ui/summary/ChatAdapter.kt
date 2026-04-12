package com.huzaif.briefly.ui.summary

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.huzaif.briefly.R
import com.huzaif.briefly.data.model.ChatMessage
import com.huzaif.briefly.databinding.ItemChatMessageBinding

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val messages = mutableListOf<ChatMessage>()

    fun setMessages(newMessages: List<ChatMessage>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    fun addMessage(message: ChatMessage) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.size

    class ChatViewHolder(private val binding: ItemChatMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.tvMessage.text = message.text
            val params = binding.cardMessage.layoutParams as LinearLayout.LayoutParams
            if (message.isUser) {
                params.gravity = Gravity.END
                binding.cardMessage.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.grad_primary_start))
                binding.tvMessage.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
            } else {
                params.gravity = Gravity.START
                binding.cardMessage.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.bg_light))
                binding.tvMessage.setTextColor(ContextCompat.getColor(binding.root.context, R.color.text_primary))
            }
            binding.cardMessage.layoutParams = params
        }
    }
}
