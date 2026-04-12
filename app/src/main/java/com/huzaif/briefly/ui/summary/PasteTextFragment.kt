package com.huzaif.briefly.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.huzaif.briefly.databinding.FragmentPasteTextBinding

class PasteTextFragment : Fragment() {

    private var _binding: FragmentPasteTextBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SummaryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasteTextBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pre-fill text if passed from file upload
        val args: PasteTextFragmentArgs by navArgs()
        args.extractedText?.let {
            binding.etPasteText.setText(it)
        }

        binding.btnSummarize.setOnClickListener {
            val text = binding.etPasteText.text.toString()
            if (text.isNotBlank()) {
                viewModel.summarizeText(text)
            } else {
                Toast.makeText(requireContext(), "Please enter some text", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSummarize.isEnabled = !isLoading
        }

        viewModel.summary.observe(viewLifecycleOwner) { summary ->
            if (summary != null) {
                val action = PasteTextFragmentDirections.actionPasteTextFragmentToResultFragment(
                    binding.etPasteText.text.toString(),
                    summary
                )
                findNavController().navigate(action)
                viewModel.clearSummary()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
