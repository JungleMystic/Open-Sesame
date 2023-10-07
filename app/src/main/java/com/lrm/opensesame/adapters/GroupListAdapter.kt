package com.lrm.opensesame.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lrm.opensesame.R
import com.lrm.opensesame.database.LoginCred
import com.lrm.opensesame.databinding.GroupListItemBinding

class GroupListAdapter(
    private val context: Context,
    private val credList: List<LoginCred>
): ListAdapter<String, GroupListAdapter.GroupItemViewHolder>(DiffCallback) {

    inner class GroupItemViewHolder(
        private val binding: GroupListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        private val rotateClockWise: Animation by lazy {
            AnimationUtils.loadAnimation(context, R.anim.rotate_clock_wise)
        }
        private val rotateAntiClockWise: Animation by lazy {
            AnimationUtils.loadAnimation(context, R.anim.rotate_anti_clock_wise)
        }

        fun bind(groupName: String) {
            binding.groupName.text = groupName
            val filteredList = credList.filter { it.group == groupName }
            val adapter = CredListAdapter(context, filteredList)
            binding.credRv.adapter = adapter

            var isExpanded = false
            binding.groupCard.setOnClickListener {
                isExpanded = !isExpanded
                if (isExpanded) {
                    binding.credRv.visibility = View.VISIBLE
                    binding.expandIcon.startAnimation(rotateClockWise)
                } else {
                    binding.credRv.visibility = View.GONE
                    binding.expandIcon.startAnimation(rotateAntiClockWise)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupItemViewHolder {
        return GroupItemViewHolder(
            GroupListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GroupItemViewHolder, position: Int) {
        val groupName = getItem(position)
        holder.bind(groupName)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}