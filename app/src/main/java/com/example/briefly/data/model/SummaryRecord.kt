package com.example.briefly.data.model

data class SummaryRecord(
    val id: String? = null,
    val userId: String? = null,
    val originalText: String? = null,
    val summary: String? = null,
    val sourceType: String? = "text",
    val timestamp: Long? = System.currentTimeMillis()
)
