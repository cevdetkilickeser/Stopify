package com.cevdetkilickeser.stopify.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VMMain @Inject constructor(
val auth: FirebaseAuth
) : ViewModel()