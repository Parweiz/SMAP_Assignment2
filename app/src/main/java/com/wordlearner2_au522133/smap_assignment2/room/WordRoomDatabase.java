package com.wordlearner2_au522133.smap_assignment2.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;


import com.wordlearner2_au522133.smap_assignment2.models.Word;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class WordRoomDatabase extends RoomDatabase {
    public abstract WordDao wordDao();

}
