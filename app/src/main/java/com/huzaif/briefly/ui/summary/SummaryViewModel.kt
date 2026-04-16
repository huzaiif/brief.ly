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
import com.huzaif.briefly.data.model.ChatMessage
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

    private val _chatMessages = MutableLiveData<List<ChatMessage>>(emptyList())
    val chatMessages: LiveData<List<ChatMessage>> = _chatMessages

    private val _isChatLoading = MutableLiveData<Boolean>()
    val isChatLoading: LiveData<Boolean> = _isChatLoading

    private val API_KEY = BuildConfig.GEMINI_API_KEY

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(GeminiApiService::class.java)

    fun summarizeText(text: String) {
        if (text.isBlank()) {
            _error.value = "Text cannot be empty"
            return
        }
        
        _isLoading.value = true
        
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
                if (API_KEY.isNullOrEmpty()) {
                    _error.value = "API Key is missing. Check local.properties"
                    _isLoading.value = false
                    return@launch
                }

                val response = apiService.generateContent(API_KEY, request)
                if (response.isSuccessful) {
                    val result = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    if (!result.isNullOrEmpty()) {
                        _summary.value = result
                    } else {
                        _error.value = "AI returned an empty summary. Try again."
                    }
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    _error.value = "API Error ${response.code()}: $errorMsg"
                }
            } catch (e: Exception) {
                _error.value = "Network Error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun askQuestion(question: String, originalText: String, summary: String) {
        if (question.isBlank()) return
        
        val currentMessages = _chatMessages.value.orEmpty().toMutableList()
        currentMessages.add(ChatMessage(question, true))
        currentMessages.add(ChatMessage("Thinking...", false))
        _chatMessages.value = currentMessages
        _isChatLoading.value = true

        val prompt = """
            Context:
            Original Document: $originalText
            Summary: $summary
            
            Question: $question
            
            Please answer the question accurately based on the provided context.
        """.trimIndent()

        val request = GeminiRequest(listOf(Content(listOf(Part(prompt)))))

        viewModelScope.launch {
            try {
                if (API_KEY.isNullOrEmpty()) {
                    _error.value = "API Key is missing."
                    _isChatLoading.value = false
                    return@launch
                }

                val response = apiService.generateContent(API_KEY, request)
                val updatedMessages = _chatMessages.value.orEmpty().toMutableList()
                
                if (updatedMessages.isNotEmpty() && updatedMessages.last().text == "Thinking...") {
                    updatedMessages.removeAt(updatedMessages.size - 1)
                }

                if (response.isSuccessful) {
                    val answer = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                        ?: "I couldn't generate an answer from the document."
                    updatedMessages.add(ChatMessage(answer, false))
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Error ${response.code()}"
                    updatedMessages.add(ChatMessage("Failed to get answer: $errorMsg", false))
                }
                _chatMessages.value = updatedMessages
            } catch (e: Exception) {
                val updatedMessages = _chatMessages.value.orEmpty().toMutableList()
                if (updatedMessages.isNotEmpty() && updatedMessages.last().text == "Thinking...") {
                    updatedMessages.removeAt(updatedMessages.size - 1)
                }
                updatedMessages.add(ChatMessage("Error: ${e.localizedMessage}", false))
                _chatMessages.value = updatedMessages
            } finally {
                _isChatLoading.value = false
            }
        }
    }

    fun saveSummary(originalText: String, summary: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().getReference("summaries").child(userId)
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

    fun clearError() {
        _error.value = null
    }

    fun clearSummary() {
        _summary.value = null
        _chatMessages.value = emptyList()
    }
}
