package com.lrm.opensesame.adapters

import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lrm.opensesame.database.LoginCred
import com.lrm.opensesame.databinding.CredListItemBinding

class CredListAdapter(
    private val context: Context,
    private val credList: List<LoginCred>
) : RecyclerView.Adapter<CredListAdapter.CredViewHolder>() {

    inner class CredViewHolder(
        private val binding: CredListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cred: LoginCred) {
            binding.username.text = cred.userName
            binding.password.text = cred.password
            binding.password.transformationMethod = PasswordTransformationMethod()

            binding.showIcon.setOnClickListener {
                binding.password.transformationMethod = null
                binding.showIcon.visibility = View.GONE
                binding.hideIcon.visibility = View.VISIBLE
            }

            binding.hideIcon.setOnClickListener {
                binding.password.transformationMethod = PasswordTransformationMethod()
                binding.hideIcon.visibility = View.GONE
                binding.showIcon.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CredViewHolder {
        return CredViewHolder(
            CredListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return credList.size
    }

    override fun onBindViewHolder(holder: CredViewHolder, position: Int) {
        val cred = credList[position]
        holder.bind(cred)
    }
}