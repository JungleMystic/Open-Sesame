package com.lrm.opensesame.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lrm.opensesame.database.LoginCred
import com.lrm.opensesame.databinding.CredListItemBinding
import com.lrm.opensesame.fragments.HomeFragmentDirections
import com.lrm.opensesame.utils.BiometricAuthListener
import com.lrm.opensesame.utils.BiometricUtils

class CredListAdapter(
    private val context: Context,
    private val credList: List<LoginCred>,
    private val fragment: Fragment
) : RecyclerView.Adapter<CredListAdapter.CredViewHolder>() {

    inner class CredViewHolder(
        private val binding: CredListItemBinding
    ) : RecyclerView.ViewHolder(binding.root), BiometricAuthListener {

        fun bind(cred: LoginCred) {
            binding.username.text = cred.userName
            binding.password.text = cred.password
            binding.password.transformationMethod = PasswordTransformationMethod()

            binding.showIcon.setOnClickListener {
                if (BiometricUtils.isBiometricReady(context)) {
                    BiometricUtils.showBiometricPrompt(
                        fragment = fragment,
                        listener = this,
                        cryptoObject = null
                    )
                } else {
                    binding.password.transformationMethod = null
                    binding.showIcon.visibility = View.GONE
                    binding.hideIcon.visibility = View.VISIBLE
                    Toast.makeText(context, "Biometric feature not available on this device...", Toast.LENGTH_SHORT).show()
                }
            }

            binding.hideIcon.setOnClickListener {
                binding.password.transformationMethod = PasswordTransformationMethod()
                binding.hideIcon.visibility = View.GONE
                binding.showIcon.visibility = View.VISIBLE
            }

            binding.username.setOnLongClickListener {
                val clipBoard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("username", cred.userName)
                clipBoard.setPrimaryClip(clip)
                true
            }

            binding.password.setOnLongClickListener {
                val clipBoard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("number", cred.password)
                clipBoard.setPrimaryClip(clip)
                true
            }

            binding.editIcon.setOnClickListener { view ->
                val action = HomeFragmentDirections.actionHomeFragmentToAddCredFragment(cred.id)
                view.findNavController().navigate(action)
            }
        }

        override fun onBiometricAuthenticateError(error: Int, errMsg: String) {
            if (error ==  BiometricPrompt.ERROR_USER_CANCELED) {
                Toast.makeText(context, "Use fingerprint to show password", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onBiometricAuthenticateSuccess(result: BiometricPrompt.AuthenticationResult) {
            binding.password.transformationMethod = null
            binding.showIcon.visibility = View.GONE
            binding.hideIcon.visibility = View.VISIBLE
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