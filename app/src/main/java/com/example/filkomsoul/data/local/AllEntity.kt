package com.example.filkomsoul.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class AllEntity(
    @Embedded val parent: MahasiswaEntity,
    @Relation(
        parentColumn = "nim",
        entityColumn = "studentId"
    )
    val children: List<ArchiveEntity>
)