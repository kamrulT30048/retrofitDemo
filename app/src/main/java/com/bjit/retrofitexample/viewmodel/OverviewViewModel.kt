
package com.bjit.retrofitexample.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bjit.retrofitexample.model.MarsPhoto
import com.bjit.retrofitexample.network.MarsApi
import kotlinx.coroutines.launch

enum class MarsApiStatus { LOADING, ERROR, DONE }

private const val TAG = "OverviewViewModel"

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<MarsApiStatus> = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of MarsPhoto
    // with new values
    private val _photos = MutableLiveData<List<MarsPhoto>>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val photos: LiveData<List<MarsPhoto>> = _photos

    init {
//        Toast.makeText(MainApplication.applicationContext(),
//            "Data is Loading", Toast.LENGTH_SHORT).show()
    }

    private fun getMarsPhotos() {

        viewModelScope.launch {
            _status.value = MarsApiStatus.LOADING
            try {
                Log.d(TAG, "getMarsPhotos: before load")
                _photos.value = MarsApi.retrofitService.getPhotos()
                _status.value = MarsApiStatus.DONE
                Log.d(TAG, "getMarsPhotos: ${status.value}")
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                _photos.value = listOf()
            }
        }
    }

    fun hitServer() {
        getMarsPhotos()
    }

    fun InternetNotConnected() {

//        Toast.makeText(
//            MainApplication.applicationContext(),
//            "Please Check Your Internet",
//            Toast.LENGTH_SHORT).show()
    }
}