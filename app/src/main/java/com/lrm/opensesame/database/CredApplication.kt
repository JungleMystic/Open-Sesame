package com.lrm.opensesame.database

import android.app.Application

class CredApplication: Application() {

    val database: CredRoomDatabase by lazy {
        CredRoomDatabase.getDatabase(this)
    }
}