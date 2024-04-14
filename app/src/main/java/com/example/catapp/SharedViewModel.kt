package com.example.catapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject

class SharedViewModel : ViewModel() {
    // Store cat breeds data
    private val _catBreeds = MutableLiveData<Map<String, JSONObject>>()
    val catBreeds: LiveData<Map<String, JSONObject>> get() = _catBreeds

    // Name of the cat selected from the spinner
    private val _selectedCat = MutableLiveData<String>()
    val selectedCat: LiveData<String> get() = _selectedCat

    // Name of the cat currently displayed in the UI
    private val _currentCat = MutableLiveData<String>()
    val currentCat: LiveData<String> get() = _currentCat

    // ImageUrl of the cat currently displayed in the UI
    private val _catImageUrl = MutableLiveData<String>()
    val catImageUrl: LiveData<String> get() = _catImageUrl

    fun setCatBreeds(catBreeds: Map<String, JSONObject>) {
        _catBreeds.value = catBreeds
    }

    fun setCatImageUrl(catImageUrl: String){
        _catImageUrl.value = catImageUrl
    }

    fun setCurrentCat(currentCat: String) {
        _currentCat.value = currentCat
    }

    fun setSelectedCat(selectedCat: String) {
        _selectedCat.value = selectedCat
    }
}