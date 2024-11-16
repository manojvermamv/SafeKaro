package com.safekaro.partner.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.safekaro.partner.model.repository.MainRepository
import com.safekaro.partner.ui.common.GlobalAction
import com.safekaro.partner.ui.common.UiAction
import com.safekaro.partner.utils.ApiDataLoader
import com.safekaro.partner.utils.Resource
import com.safekaro.partner.utils.SingleEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class MainActivityViewModel(
    context: Application,
    private val mainRepository: MainRepository,
) : AndroidViewModel(context) {

    val userDataRes = ApiDataLoader { mainRepository.getUserDataApiCall() }

    private var mStartDate = MutableLiveData<Date>()
    private var mEndDate = MutableLiveData<Date>()

    fun setDashBoardDate(startDate: Date, endDate: Date) {
        mStartDate.value = startDate
        mEndDate.value = endDate
    }

    fun getDashboardData(partnerId: String, startDate: String, endDate: String) = liveData {
        emit(Resource.Loading)
        emit(mainRepository.getPartnerDashboard(startDate, endDate, partnerId))
    }

    fun getRankData(partnerId: String) = liveData {
        emit(Resource.Loading)
        emit(mainRepository.getRankData(partnerId))
    }

    fun getWalletCreditDebit(partnerId: String, startDate: String, endDate: String) = liveData {
        emit(Resource.Loading)
        emit(mainRepository.getWalletCreditDebit(startDate, endDate, partnerId))
    }

//    val upcomingMatchesRes = ApiDataLoaderPaged<Match, UpcomingMatches> { page ->
//        mainRepository.getUpcomingMatchesApiCall(page)
//    }

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

    /**
     * StateFlow: Use when you need to hold and expose the latest state (e.g., UI state, form data)
     * */
    private val _uiActions = MutableStateFlow<UiAction<Any>>(UiAction.Ideal)
    val uiAction: StateFlow<UiAction<Any>> = _uiActions.asStateFlow()

    fun uiAction(action: UiAction<Any>) {
        viewModelScope.launch {
            _uiActions.value = action
        }
    }

//    fun updateWallet(walletSummary: WalletSummary) {
//        viewModelScope.launch {
//            _uiActions.value = UiAction.UpdateWallet(walletSummary)
//        }
//    }

}