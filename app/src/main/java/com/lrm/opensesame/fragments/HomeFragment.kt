package com.lrm.opensesame.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.lrm.opensesame.R
import com.lrm.opensesame.adapters.GroupListAdapter
import com.lrm.opensesame.constants.TAG
import com.lrm.opensesame.database.CredApplication
import com.lrm.opensesame.databinding.FragmentHomeBinding
import com.lrm.opensesame.viewmodel.CredViewModel
import com.lrm.opensesame.viewmodel.CredViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.blue)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        credViewModel.getAllCredentials.observe(this.viewLifecycleOwner) { credList ->
            Log.i(TAG, "onViewCreated: getAllData -> $credList")
            credViewModel.setGroupNameList(credList)

            val adapter = GroupListAdapter(requireContext(), credList)
            binding.groupNameRv.adapter = adapter
            credViewModel.groupNameList.observe(this.viewLifecycleOwner) {list ->
                Log.i(TAG, "onViewCreated: groupNameList -> $list")
                list.let { adapter.submitList(it) }
            }
        }

        binding.addCredFab.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddCredFragment()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}