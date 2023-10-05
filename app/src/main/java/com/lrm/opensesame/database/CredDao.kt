package com.lrm.opensesame.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CredDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cred: LoginCred)

    @Update
    suspend fun update(cred: LoginCred)

    @Delete
    suspend fun delete(cred: LoginCred)

    @Query("SELECT * FROM cred_table ORDER BY group_name ASC")
    fun getAllCredentials(): Flow<List<LoginCred>>

    @Query("SELECT * FROM cred_table WHERE id = :id")
    fun getLoginCred(id: Int): Flow<LoginCred>
}