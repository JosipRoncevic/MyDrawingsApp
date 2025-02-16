package org.unizd.rma.roncevic.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.unizd.rma.roncevic.data.local.entity.DrawingEntity

@Dao
interface DrawingDao {

    @Query("SELECT * FROM DrawingEntity")
    fun getAllDrawings(): Flow<List<DrawingEntity>>

    @Query("""SELECT * FROM DrawingEntity WHERE id = :id""")
    suspend fun getDrawingById(id: Int): DrawingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrawing(drawingEntity: DrawingEntity)

    @Delete
    suspend fun deleteDrawing(drawingEntity: DrawingEntity)

    @Update
    suspend fun updateDrawing(drawingEntity: DrawingEntity)
}