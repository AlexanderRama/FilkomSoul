package com.example.filkomsoul.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MahasiswaEntity(
    @field:ColumnInfo(name = "nim")
    @field:PrimaryKey
    val nim: Long,

    @field:ColumnInfo(name = "name")
    val name: String,

    @field:ColumnInfo(name = "gender")
    val gender: String,

    @field:ColumnInfo(name = "major")
    val major: String,

    @field:ColumnInfo(name = "year")
    val year: String,
)