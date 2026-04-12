package com.huzaif.briefly.ui.scan

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.huzaif.briefly.databinding.FragmentScanSummarizeBinding
import com.huzaif.briefly.ui.summary.SummaryViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class ScanSummarizeFragment : Fragment() {

    private var _binding: FragmentScanSummarizeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SummaryViewModel by viewModels()

    private var imageUri: Uri? = null
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera()
        } else {
            Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                imageUri = uri
                binding.ivPreview.setImageURI(uri)
                binding.ivPreview.alpha = 1.0f
                binding.tvPlaceholder.visibility = View.GONE
                runOcr(uri)
            }
        }
    }

    private val captureImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri?.let { uri ->
                binding.ivPreview.setImageURI(uri)
                binding.ivPreview.alpha = 1.0f
                binding.tvPlaceholder.visibility = View.GONE
                runOcr(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanSummarizeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        binding.btnCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) 
                == PackageManager.PERMISSION_GRANTED) {
                launchCamera()
            } else {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        binding.btnSummarize.setOnClickListener {
            val text = binding.etExtractedText.text.toString()
            if (text.isNotBlank()) {
                viewModel.summarizeText(text)
            } else {
                Toast.makeText(requireContext(), "No text to summarize", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSummarize.isEnabled = !isLoading
        }

        viewModel.summary.observe(viewLifecycleOwner) { summary ->
            if (summary != null) {
                val action = ScanSummarizeFragmentDirections.actionScanSummarizeFragmentToResultFragment(
                    binding.etExtractedText.text.toString(),
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

    private fun launchCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
        imageUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        captureImageLauncher.launch(intent)
    }

    private fun runOcr(uri: Uri) {
        binding.progressBar.visibility = View.VISIBLE
        val image = InputImage.fromFilePath(requireContext(), uri)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                binding.progressBar.visibility = View.GONE
                if (visionText.text.isNotBlank()) {
                    binding.etExtractedText.setText(visionText.text)
                } else {
                    Toast.makeText(requireContext(), "No readable text detected", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "OCR Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
