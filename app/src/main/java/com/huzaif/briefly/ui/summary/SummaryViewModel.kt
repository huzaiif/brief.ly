package com.huzaif.briefly.ui.summary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huzaif.briefly.BuildConfig
import com.huzaif.briefly.data.api.Content
import com.huzaif.briefly.data.api.GeminiApiService
import com.huzaif.briefly.data.api.GeminiRequest
import com.huzaif.briefly.data.api.Part
import com.huzaif.briefly.data.model.SummaryRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SummaryViewModel : ViewModel() {

    private val _summary = MutableLiveData<String?>()
    val summary: LiveData<String?> = _summary

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> = _saveSuccess

    private val API_KEY = BuildConfig.GEMINI_API_KEY

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(GeminiApiService::class.java)

    fun summarizeText(text: String) {
        _isLoading.value = true
        // Enhanced prompt for better formatting
        val prompt = """
            Summarize the following text with a professional and elegant structure. 
            Use the following format:
            
            📌 OVERVIEW
            [A concise 2-3 sentence paragraph summarizing the core message]
            
            🚀 KEY INSIGHTS
            • [Insight 1]
            • [Insight 2]
            • [Insight 3]
            • [Insight 4]
            • [Insight 5]
            
            💡 TAKEAWAY
            [A final concluding thought or action item]
            
            Text to summarize:
            $text
        """.trimIndent()
        
        val request = GeminiRequest(listOf(Content(listOf(Part(prompt)))))

        viewModelScope.launch {
            try {
                if (API_KEY.isEmpty()) {
                    _error.value = "Error: API Key is missing. Please add GEMINI_API_KEY to local.properties"
                    _isLoading.value = false
                    return@launch
                }

                val response = apiService.generateContent(API_KEY, request)
                if (response.isSuccessful) {
                    _summary.value = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                } else {
                    val errorBody = response.errorBody()?.string()
                    _error.value = "API Error: ${response.code()} ${response.message()} - $errorBody"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveSummary(originalText: String, summary: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("summaries")
        val id = database.push().key ?: return
        val record = SummaryRecord(id, userId, originalText, summary)

        database.child(id).setValue(record).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _saveSuccess.value = true
            } else {
                _error.value = task.exception?.message
            }
        }
    }

    fun clearSummary() {
        _summary.value = null
    }
}
