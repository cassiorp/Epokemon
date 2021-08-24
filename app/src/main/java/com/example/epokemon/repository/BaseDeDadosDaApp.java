package com.example.epokemon.repository;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RotinaModel.class}, version = 5, exportSchema = false)
public abstract class BaseDeDadosDaApp extends RoomDatabase {
    public abstract RotinaDAO getRotinaDAO();

    private static volatile BaseDeDadosDaApp instance;

    public static BaseDeDadosDaApp getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static BaseDeDadosDaApp create(final Context context) {
        return Room.databaseBuilder(
                context,
                BaseDeDadosDaApp.class,
                "pdm2_android")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
//    private val MIGRATION_1_2 = object : Migration(1, 2) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            // empty migration.
//        }
//    }

}

