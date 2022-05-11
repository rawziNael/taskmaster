package com.example.taskmaster.Model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

    //1
    private static AppDatabase taskData; //declaration for the instance

    //2
    public AppDatabase() {

    }

    //3
    public static synchronized AppDatabase getInstance(Context context) {

        if(taskData == null)
        {
            taskData = Room.databaseBuilder(context,
                    AppDatabase.class, "AppDatabase").allowMainThreadQueries().build();
        }
        return taskData;
    }
}
