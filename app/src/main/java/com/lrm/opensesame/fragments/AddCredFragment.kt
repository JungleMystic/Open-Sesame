package com.lrm.opensesame.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lrm.opensesame.R
import com.lrm.opensesame.constants.TAG
import com.lrm.opensesame.database.CredApplication
import com.lrm.opensesame.database.LoginCred
import com.lrm.opensesame.databinding.FragmentAddCredBinding
import com.lrm.opensesame.viewmodel.CredViewModel
import com.lrm.opensesame.viewmodel.CredViewModelFactory

class AddCredFragment : Fragment() {

    private var _binding: FragmentAddCredBinding? = null
    private val binding get() = _binding!!

    private val credViewModel: CredViewModel by activityViewModels {
        CredViewModelFactory(
            (activity?.application as CredApplication).database.credDao()
        )
    }

    private lateinit var groupNameList: MutableList<String>

    private val navigationArgs: AddCredFragmentArgs by navArgs()
    private lateinit var cred: LoginCred

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCredBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backIcon.setOnClickListener { goBack() }
        binding.cancel.setOnClickListener { goBack() }

        credViewModel.groupNameList.observe(this.viewLifecycleOwner) { list ->
            groupNameList = list.toMutableList()
            groupNameList.add("Others")
            Log.i(TAG, "onViewCreated: groupNameList -> $groupNameList")
        }

        val id = navigationArgs.id

        if (id > 0) {
            binding.fragmentLabel.text = resources.getString(R.string.edit_credentials)
            binding.deleteIcon.visibility = View.VISIBLE
            credViewModel.retrieveCred(id).observe(this.viewLifecycleOwner) {
                bind(it)
            }
        } else {
            binding.fragmentLabel.text = resources.getString(R.string.add_credentials)
            binding.deleteIcon.visibility = View.GONE
            binding.save.setOnClickListener { addCred() }
        }
    }

    override fun onResume() {
        super.onResume()
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_tv, groupNameList)
        binding.groupName.setAdapter(arrayAdapter)
    }

    private fun bind(cred: LoginCred) {
        binding.groupName.setText(cred.group, TextView.BufferType.SPANNABLE)
        binding.username.setText(cred.userName, TextView.BufferType.SPANNABLE)
        binding.password.setText(cred.password, TextView.BufferType.SPANNABLE)

        binding.save.setOnClickListener { updateEvent(cred.id) }
        binding.deleteIcon.setOnClickListener { showDeleteDialog(cred) }
    }

    private fun addCred() {
        val group = binding.groupName.text.toString().trim()
        val userName = binding.username.text.toString().trim()
        val password = binding.password.text.toString()

        if (credViewModel.isEntryValid(requireContext(),
            group, userName, password)) {
            credViewModel.addCred(group, userName, password)
            Toast.makeText(requireContext(), "Successfully added...", Toast.LENGTH_SHORT).show()
            goBack()
        }
    }

    private fun updateEvent(id: Int) {
        val group = binding.groupName.text.toString().trim()
        val userName = binding.username.text.toString().trim()
        val password = binding.password.text.toString()

        if (credViewModel.isEntryValid(requireContext(), group, userName, password)) {
            credViewModel.getUpdatedCred(id, group, userName, password)
            Toast.makeText(requireContext(), "Successfully updated...", Toast.LENGTH_SHORT).show()
            goBack()
        }
    }

    private fun showDeleteDialog(cred: LoginCred) {
        val dialogView = requireActivity().layoutInflater.inflate(R.layout.custom_delete_dialog, null)
        val yesTv = dialogView.findViewById<TextView>(R.id.yes_tv)
        val noTv = dialogView.findViewById<TextView>(R.id.no_tv)

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
        builder.setCancelable(true)

        val deleteDialog = builder.create()
        deleteDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        deleteDialog.show()

        yesTv.setOnClickListener {
            deleteDialog.dismiss()
            goBack()
            Handler(Looper.getMainLooper()).postDelayed({
                credViewModel.deleteCred(cred)
            }, 1000)
        }

        noTv.setOnClickListener {
            deleteDialog.dismiss()
        }
    }

    private fun goBack() {
        this.findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}