package com.lrm.opensesame.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
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

    private lateinit var adapter: GroupListAdapter
    private lateinit var groupNameList: List<String>

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

            credViewModel.groupNameList.observe(this.viewLifecycleOwner) {list ->
                groupNameList = list
                Log.i(TAG, "onViewCreated: groupNameList -> $list")
                adapter = GroupListAdapter(requireContext(), groupNameList, credList, this)
                binding.groupNameRv.adapter = adapter
            }
        }

        binding.searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchBar.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        binding.addCredFab.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAddCredFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<String>()
            for (i in groupNameList) {
                if (i.lowercase().contains(query.lowercase())) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isNotEmpty()) {
                adapter.setFilteredList(filteredList)
            } else {
                Toast.makeText(requireContext(), "Group not found...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop is called")
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}