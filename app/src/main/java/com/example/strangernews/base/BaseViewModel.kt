package com.example.strangernews.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.strangernews.utils.DataResult
import kotlinx.coroutines.*
import java.lang.Exception

open class BaseViewModel : ViewModel() {

    private var loadingCount = 0
    protected val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        errorResponse.postValue(exception)
    }
    val isLoading = MutableLiveData<Boolean>().apply { value = false }
    val errorResponse = MutableLiveData<Throwable?>().apply { value = null }

    protected fun <T> launchTaskSync(
        onRequest: suspend CoroutineScope.() -> DataResult<T>,
        onSuccess: (T) -> Unit = {},
        onError: (Exception) -> Unit = {},
        isShowLoading: Boolean = true
    ) = viewModelScope.launch {
        showLoading(isShowLoading)
        when (val asynchronousTasks = onRequest(this)) {
            is DataResult.Success -> onSuccess(asynchronousTasks.data)
            is DataResult.Error -> {
                asynchronousTasks.exception.let {
                    errorResponse.value = it
                    onError(it)
                }
            }
            else -> {}
        }
        hideLoading(isShowLoading)
    }

    protected fun safeCall(request: suspend () -> Unit) = viewModelScope.launch {
        showLoading(true)
        try {
            request()
        } catch (e: Exception) {
            errorResponse.postValue(e)
        }
        hideLoading(true)
    }

    private fun showLoading(isShowLoading: Boolean) {
        if (!isShowLoading) return
        loadingCount++
        if (isLoading.value != true && loadingCount > 0) isLoading.value = true
    }

    private fun hideLoading(isShowLoading: Boolean) {
        if (!isShowLoading) return
        loadingCount--
        if (loadingCount <= 0) {
            loadingCount = 0
            isLoading.value = false
        }
    }
}
