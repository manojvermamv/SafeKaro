package com.safekaro.partner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.safekaro.partner.model.data.LoginUserRequest
import com.safekaro.partner.model.repository.MainRepository
import com.safekaro.partner.ui.common.GlobalAction
import com.safekaro.partner.utils.Resource
import com.safekaro.partner.utils.SingleEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    context: Application,
    private val mainRepository: MainRepository,
) : AndroidViewModel(context) {

    fun loginWithMail(loginRequest: LoginUserRequest) = liveData {
        emit(Resource.Loading)
        emit(mainRepository.loginWithMail(loginRequest))
    }

    /**
     * UI actions as event, user action is single one time event, Shouldn't be multiple time consumption
     */

    private val _progressBar = MutableLiveData<SingleEvent<Boolean>>()
    val progressBar: LiveData<SingleEvent<Boolean>> get() = _progressBar

    fun triggerProgressBar(show: Boolean) {
        _progressBar.value = SingleEvent(show)
    }

    /**
     * SharedFlow: Use for one-time events like navigation commands, toast messages, or any actions where you're interested in broadcasting events but not holding state
     * */
    private val _globalActions = MutableSharedFlow<GlobalAction<Any>>()
    val globalAction: SharedFlow<GlobalAction<Any>> = _globalActions.asSharedFlow()

    fun globalAction(action: GlobalAction<Any>) {
        viewModelScope.launch {
            _globalActions.emit(action)
        }
    }

}