package org.unizd.rma.roncevic.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.unizd.rma.roncevic.data.local.dao.DrawingDao
import org.unizd.rma.roncevic.data.mapper.asExternalModel
import org.unizd.rma.roncevic.data.mapper.toEntity
import org.unizd.rma.roncevic.domain.model.Drawing
import org.unizd.rma.roncevic.domain.repository.DrawingRepository

class DrawingRepositoryImpl(
    private val dao: DrawingDao
):DrawingRepository {
    override fun getAllDrawings(): Flow<List<Drawing>> {
        return dao.getAllDrawings()
            .map { drawings ->
                drawings.map {
                    it.asExternalModel()
                }

            }

    }

    override suspend fun getDrawingById(id: Int): Drawing? {
        return dao.getDrawingById(id)?.asExternalModel()
    }

    override suspend fun insertDrawing(drawing: Drawing) {
        dao.insertDrawing(drawing.toEntity())
    }

    override suspend fun deleteDrawing(drawing: Drawing) {
        dao.deleteDrawing(drawing.toEntity())
    }

    override suspend fun updateDrawing(drawing: Drawing) {
        dao.updateDrawing(drawing.toEntity())
    }
}