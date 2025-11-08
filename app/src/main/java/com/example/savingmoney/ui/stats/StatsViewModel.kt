package com.example.savingmoney.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.savingmoney.domain.usecase.GetMonthlySummaryUseCase
import com.example.savingmoney.domain.model.TransactionSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

// Trạng thái UI cho màn hình Báo cáo
data class StatsUiState(
    val isLoading: Boolean = false,
    val summary: TransactionSummary? = null,
    val error: String? = null
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getMonthlySummaryUseCase: GetMonthlySummaryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState

    init {
        loadMonthlySummary()
    }

    fun loadMonthlySummary(month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1, year: Int = Calendar.getInstance().get(Calendar.YEAR)) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = getMonthlySummaryUseCase(month, year)
            result.onSuccess { summary ->
                _uiState.value = _uiState.value.copy(summary = summary, isLoading = false)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(error = e.message ?: "Lỗi tải báo cáo", isLoading = false)
            }
        }
    }
}