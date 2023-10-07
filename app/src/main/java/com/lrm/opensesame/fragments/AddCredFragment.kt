package com.lrm.opensesame.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lrm.opensesame.R
import com.lrm.opensesame.database.CredApplication
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

        binding.backIcon.setOnClickListener { this.findNavController().navigateUp() }
        binding.cancel.setOnClickListener { this.findNavController().navigateUp() }

        binding.fragmentLabel.text = resources.getString(R.string.add_credentials)
        binding.save.setOnClickListener { addCred() }
    }

    private fun addCred() {
        val group = binding.groupName.text.toString().trim()
        val userName = binding.username.text.toString().trim()
        val password = binding.password.text.toString()

        if (credViewModel.isEntryValid(requireContext(),
            group, userName, password)) {
            credViewModel.addCred(group, userName, password)
            this.findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}