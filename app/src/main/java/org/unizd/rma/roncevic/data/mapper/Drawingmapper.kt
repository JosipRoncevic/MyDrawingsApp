package org.unizd.rma.roncevic.data.mapper

import org.unizd.rma.roncevic.data.local.entity.DrawingEntity
import org.unizd.rma.roncevic.domain.model.Drawing


fun DrawingEntity.asExternalModel(): Drawing = Drawing(
    id, title,author
)

fun Drawing.toEntity():DrawingEntity=DrawingEntity(
    id, title,author
)