package com.example.briefly.ui.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.briefly.databinding.FragmentSavedSummariesBinding

class SavedSummariesFragment : Fragment() {

    private var _binding: FragmentSavedSummariesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SavedSummariesViewModel by viewModels()
    private lateinit var adapter: SummaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedSummariesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SummaryAdapter(
            onItemClick = { record ->
                val action = SavedSummariesFragmentDirections.actionSavedSummariesFragmentToResultFragment(
                    record.originalText ?: "",
                    record.summary ?: ""
                )
                findNavController().navigate(action)
            },
            onDeleteClick = { id ->
                viewModel.deleteSummary(id)
            }
        )

        binding.rvSummaries.adapter = adapter

        viewModel.summaries.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvNoSummaries.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.fetchSummaries()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
