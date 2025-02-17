package org.unizd.rma.roncevic.ui.screen.drawing

sealed interface DrawingEvent {
    data class TitleChange(val value: String): DrawingEvent
    data class AuthorChange(val value: String): DrawingEvent
    data class ThemeChange(val value: String) : DrawingEvent
    data class DateChange(val value: String) : DrawingEvent
    object Save: DrawingEvent
    object NavigateBack: DrawingEvent
    object DeleteDrawing: DrawingEvent
}