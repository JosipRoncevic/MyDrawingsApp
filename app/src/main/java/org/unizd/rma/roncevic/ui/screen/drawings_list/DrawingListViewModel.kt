package org.unizd.rma.roncevic.ui.screen.drawings_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.unizd.rma.roncevic.domain.repository.DrawingRepository
import javax.inject.Inject

@HiltViewModel
class DrawingListViewModel @Inject constructor(
    private val repository: DrawingRepository
): ViewModel() {

    val drawingList = repository.getAllDrawings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = emptyList()

    )
}