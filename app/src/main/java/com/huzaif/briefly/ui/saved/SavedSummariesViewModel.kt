package com.huzaif.briefly.ui.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huzaif.briefly.data.model.SummaryRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SavedSummariesViewModel : ViewModel() {

    private val _summaries = MutableLiveData<List<SummaryRecord>>()
    val summaries: LiveData<List<SummaryRecord>> = _summaries

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val database = FirebaseDatabase.getInstance().getReference("summaries")

    fun fetchSummaries() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        _isLoading.value = true
        
        database.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<SummaryRecord>()
                    for (child in snapshot.children) {
                        child.getValue(SummaryRecord::class.java)?.let { list.add(it) }
                    }
                    _summaries.value = list.reversed()
                    _isLoading.value = false
                }

                override fun onCancelled(error: DatabaseError) {
                    _isLoading.value = false
                }
            })
    }

    fun deleteSummary(id: String) {
        database.child(id).removeValue()
    }
}
