package com.example.filkomsoul.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM ArchiveEntity")
    fun getResult(): LiveData<List<ArchiveEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMahasiswa(parentEntity: MahasiswaEntity)

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertResult(user: ArchiveEntity)

    @Query("DELETE FROM MahasiswaEntity")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM MahasiswaEntity")
    fun getAll(): LiveData<List<AllEntity>>
}