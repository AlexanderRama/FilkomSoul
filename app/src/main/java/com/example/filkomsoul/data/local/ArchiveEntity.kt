package com.example.filkomsoul.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = MahasiswaEntity::class,
            parentColumns = ["nim"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ArchiveEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    val outputId: Int?,

    @field:ColumnInfo(name = "studentId")
    val studentId: Long,

    @field:ColumnInfo(name = "total")
    val total: Int,

    @field:ColumnInfo(name = "result")
    val result: String,

    @field:ColumnInfo(name = "date")
    val date: String,
    )