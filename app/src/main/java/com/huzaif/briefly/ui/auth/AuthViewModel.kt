package com.huzaif.briefly.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> = _user

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        _user.value = auth.currentUser
    }

    fun login(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.value = auth.currentUser
                } else {
                    _error.value = task.exception?.message
                }
            }
    }

    fun signup(email: String, pass: String, name: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    auth.currentUser?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                _user.value = auth.currentUser
                            } else {
                                _error.value = profileTask.exception?.message
                            }
                        }
                } else {
                    _error.value = task.exception?.message
                }
            }
    }

    fun logout() {
        auth.signOut()
        _user.value = null
    }

    fun clearError() {
        _error.value = null
    }
}
