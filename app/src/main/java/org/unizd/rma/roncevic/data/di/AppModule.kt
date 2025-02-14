package org.unizd.rma.roncevic.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.unizd.rma.roncevic.data.local.DrawingDataBase
import org.unizd.rma.roncevic.data.repository.DrawingRepositoryImpl
import org.unizd.rma.roncevic.domain.repository.DrawingRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDrawingDataBase(@ApplicationContext context: Context): DrawingDataBase=
        Room.databaseBuilder(
            context,
            DrawingDataBase::class.java,
            DrawingDataBase.name
        ).build()

    @Provides
    @Singleton
    fun provideDrawingRepository(dataBase: DrawingDataBase): DrawingRepository =
        DrawingRepositoryImpl(dao = dataBase.dao)
}