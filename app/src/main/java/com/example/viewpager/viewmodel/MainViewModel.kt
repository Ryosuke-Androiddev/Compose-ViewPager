package com.example.viewpager.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viewpager.api.VocabularyApi
import com.example.viewpager.ui.navigation.model.WordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val api: VocabularyApi
): ViewModel() {

    private val _state = mutableStateOf(WordState())
    val state: State<WordState> = _state

    init {
        getRandomRabbit()
    }

    fun getRandomRabbit() {
        viewModelScope.launch {
            try {
                _state.value = state.value.copy(isLoading = true)
                _state.value = state.value.copy(
                    word = api.getRandomWords(),
                    isLoading = false
                )
            } catch(e: Exception) {
                Log.e("MainViewModel", "getRandomRabbit: ", e)
                _state.value = state.value.copy(isLoading = false)
            }
        }
    }

}