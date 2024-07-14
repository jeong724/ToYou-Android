package com.toyou.toyouandroid.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toyou.toyouandroid.di.FragmentNavigator
import com.toyou.toyouandroid.model.HomeOptionResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeResultViewModel (
    private val navigator: FragmentNavigator
) : ViewModel() {

    private val _sentence = MutableLiveData<HomeOptionResult>()
    val sentence: LiveData<HomeOptionResult> get() = _sentence

    private val _selectedStamp = MutableLiveData<String>()
    val selectedStamp: LiveData<String> get() = _selectedStamp

    fun setSelectedStamp(stamp: String) {
        _selectedStamp.value = stamp
        viewModelScope.launch {
            delay(2000)
            navigator.navigateToHomeFragment()
        }
    }
}
