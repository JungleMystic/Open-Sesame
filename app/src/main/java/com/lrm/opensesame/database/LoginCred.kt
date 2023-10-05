package com.lrm.opensesame.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cred_table")
data class LoginCred(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "group_name")
    val group: String,
    @ColumnInfo(name = "username")
    val userName: String,
    @ColumnInfo(name = "password")
    val password: String
)
