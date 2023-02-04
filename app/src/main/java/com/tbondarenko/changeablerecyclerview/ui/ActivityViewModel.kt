package com.tbondarenko.changeablerecyclerview.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tbondarenko.changeablerecyclerview.data.model.Number
import com.tbondarenko.changeablerecyclerview.data.NumberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val repository: NumberRepository,
) : ViewModel() {

    private val _numberState = MutableStateFlow(repository.numberList)
    val numberState: StateFlow<List<Number>> = _numberState

    init {
        getAllNumbersState()
    }

    private fun getAllNumbersState() = viewModelScope.launch {
        repository.addItem1().collect { list ->
            _numberState.value = list
        }
    }

    fun removeNumberState(number: Number) = viewModelScope.launch {
        repository.removeNumber1(number).collect { list ->
            _numberState.value = list
        }
    }
}
