package com.myjar.jarassignment.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjar.jarassignment.createRetrofit
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.repository.JarRepository
import com.myjar.jarassignment.data.repository.JarRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JarViewModel : ViewModel() {

    private val _listStringData = MutableStateFlow<List<ComputerItem>>(emptyList())
    val listStringData: StateFlow<List<ComputerItem>>
        get() = _listStringData
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String>
        get() = _searchQuery
    private val repository: JarRepository = JarRepositoryImpl(createRetrofit())

    fun fetchData() {
        viewModelScope.launch {
          repository.fetchResults().collect {
               _listStringData.value = it
           }
        }
    }
}