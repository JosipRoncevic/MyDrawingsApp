package org.unizd.rma.roncevic.domain.repository

import kotlinx.coroutines.flow.Flow
import org.unizd.rma.roncevic.domain.model.Drawing

interface DrawingRepository {

    fun getAllDrawings(): Flow<List<Drawing>>

    suspend fun getDrawingById(id: Int): Drawing?

    suspend fun insertDrawing(drawing: Drawing)

    suspend fun deleteDrawing(drawing: Drawing)

    suspend fun updateDrawing(drawing: Drawing)
}