package com.example.filkomsoul.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.filkomsoul.data.local.AllEntity
import com.example.filkomsoul.data.local.ArchiveEntity
import com.example.filkomsoul.data.local.MahasiswaEntity
import com.example.filkomsoul.data.local.UserDao
import com.example.filkomsoul.data.local.UserDatabase

class UserRepository (private val usersDao: UserDao) {

    suspend fun insertResult(result : ArchiveEntity) {
        try {
            usersDao.insertResult(result)
        } catch (e: Exception) {
            Log.d("UserRepository", "${e.message.toString()} ")
        }
    }

    suspend fun insertMahasiswa(mahasiswa: MahasiswaEntity) {
        try {
            usersDao.insertMahasiswa(mahasiswa)
        } catch (e: Exception) {
            Log.d("UserRepository", "${e.message.toString()} ")
        }
    }

    fun getResult(): LiveData<List<ArchiveEntity>> {
        return usersDao.getResult()
    }

    suspend fun delete() {
        try {
            usersDao.deleteAll()
        } catch (e: Exception) {
            Log.d("UserRepository", "${e.message.toString()} ")
        }
    }

    fun getAllData(): LiveData<List<AllEntity>> {
        return usersDao.getAll()
    }



    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(context: Context): UserRepository {
            return instance ?: synchronized(this) {
                if(instance == null) {
                    val database = UserDatabase.getDatabase(context)
                    instance = UserRepository(database.userDao()
                    )
                }
                return instance as UserRepository
            }
        }
    }
}