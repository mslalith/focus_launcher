package dev.mslalith.focuslauncher.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _isInContextualMode = MutableStateFlow(value = false)
    val isInContextualMode = _isInContextualMode.asStateFlow()

    fun showContextualMode() {
        with(_isInContextualMode) {
            if (!value) value = true
        }
    }

    fun hideContextualMode() {
        with(_isInContextualMode) {
            if (value) value = false
        }
    }
}
