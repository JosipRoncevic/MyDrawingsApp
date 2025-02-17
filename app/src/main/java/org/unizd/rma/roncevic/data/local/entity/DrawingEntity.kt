package org.unizd.rma.roncevic.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DrawingEntity(
    @PrimaryKey val id: Int?,
    val title: String,
    val author: String,
    val theme: String,
    val date: String
)
