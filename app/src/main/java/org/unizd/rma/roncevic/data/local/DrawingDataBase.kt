package org.unizd.rma.roncevic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.unizd.rma.roncevic.data.local.dao.DrawingDao
import org.unizd.rma.roncevic.data.local.entity.DrawingEntity

@Database(
    version = 1,
    entities = [DrawingEntity::class]
)
abstract class DrawingDataBase: RoomDatabase(){

    abstract val dao:DrawingDao

    companion object{
        const val name = "drawing_db"
    }
}