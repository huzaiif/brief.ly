package com.huzaif.briefly.ui.saved

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.huzaif.briefly.data.model.SummaryRecord
import com.huzaif.briefly.databinding.ItemSummaryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SummaryAdapter(
    private val onItemClick: (SummaryRecord) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : ListAdapter<SummaryRecord, SummaryAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: ItemSummaryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: SummaryRecord, onItemClick: (SummaryRecord) -> Unit, onDeleteClick: (String) -> Unit) {
            binding.tvSummaryPreview.text = record.summary
            
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            binding.tvDate.text = sdf.format(Date(record.timestamp ?: 0))

            binding.root.setOnClickListener { onItemClick(record) }
            binding.btnDelete.setOnClickListener { record.id?.let { onDeleteClick(it) } }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick, onDeleteClick)
    }

    object DiffCallback : DiffUtil.ItemCallback<SummaryRecord>() {
        override fun areItemsTheSame(oldItem: SummaryRecord, newItem: SummaryRecord): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: SummaryRecord, newItem: SummaryRecord): Boolean = oldItem == newItem
    }
}
