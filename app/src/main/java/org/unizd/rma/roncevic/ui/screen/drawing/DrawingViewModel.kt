package org.unizd.rma.roncevic.ui.screen.drawing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.unizd.rma.roncevic.domain.model.Drawing
import org.unizd.rma.roncevic.domain.repository.DrawingRepository
import org.unizd.rma.roncevic.ui.util.UiEvent
import javax.inject.Inject

@HiltViewModel
class DrawingViewModel @Inject constructor(
    private val repository:DrawingRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(DrawingState())
    val state = _state.asStateFlow()

    private val _event = Channel<UiEvent>()
    val event = _event.receiveAsFlow()

    private fun sendEvent(event:UiEvent ){
        viewModelScope.launch {
            _event.send(event)
        }
    }

    init{
        savedStateHandle.get<String>("id")?.let {
            val id = it.toInt()
            viewModelScope.launch {
                repository.getDrawingById(id)?.let { drawing->
                    _state.update {screenState ->
                        screenState.copy(
                            id =drawing.id,
                            title = drawing.title,
                            author = drawing.author,
                            theme = drawing.theme,
                            date = drawing.date,
                            imageUri = drawing.imageUri
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: DrawingEvent){
        when (event){
            is DrawingEvent.AuthorChange ->{
                _state.update {
                    it.copy(
                    author = event.value
                    )
                }
            }
            is DrawingEvent.TitleChange -> {
                _state.update {
                    it.copy(
                        title = event.value
                    )
                }
            }
            is DrawingEvent.ThemeChange ->{
                _state.update {
                    it.copy(
                        theme = event.value
                    )
                }
            }
            is DrawingEvent.DateChange -> {
                _state.update {
                    it.copy(
                        date = event.value
                    )
                }
            }
            is DrawingEvent.ImageChange -> {
                _state.update {
                    it.copy(
                        imageUri = event.value)
                }
            }


            DrawingEvent.NavigateBack -> sendEvent(UiEvent.NavigateBack)
            DrawingEvent.Save -> {
                viewModelScope.launch {
                    val state= state.value
                    val drawing = Drawing(
                        id = state.id,
                        title = state.title,
                        author = state.author,
                        theme = state.theme,
                        date = state.date,
                        imageUri = state.imageUri
                    )
                    if(state.id==null){
                        repository.insertDrawing(drawing)
                    }
                    else{
                        repository.updateDrawing(drawing)
                    }
                    sendEvent(UiEvent.NavigateBack)
                }

            }

            DrawingEvent.DeleteDrawing ->{
                viewModelScope.launch {
                    val state = state.value
                    val drawing = Drawing(
                        id = state.id,
                        title = state.title,
                        author = state.author,
                        theme = state.theme,
                        date = state.date,
                        imageUri = state.imageUri
                    )
                    repository.deleteDrawing(drawing)
                }
                sendEvent(UiEvent.NavigateBack)
            }

        }
    }
}