package com.lrm.opensesame.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lrm.opensesame.R
import com.lrm.opensesame.database.LoginCred
import com.lrm.opensesame.databinding.GroupListItemBinding

class GroupListAdapter(
    private val context: Context,
    private var groupNameList: List<String>,
    private val credList: List<LoginCred>,
    private val fragment: Fragment
): RecyclerView.Adapter<GroupListAdapter.GroupItemViewHolder>() {

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
            val adapter = CredListAdapter(context, filteredList, fragment)
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

    override fun getItemCount(): Int {
        return groupNameList.size
    }

    override fun onBindViewHolder(holder: GroupItemViewHolder, position: Int) {
        val groupName = groupNameList[position]
        holder.bind(groupName)
    }

    fun setFilteredList(newList: List<String>) {
        this.groupNameList = newList
        notifyDataSetChanged()
    }
}