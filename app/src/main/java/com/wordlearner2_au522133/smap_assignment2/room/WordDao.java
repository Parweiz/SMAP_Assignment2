package com.wordlearner2_au522133.smap_assignment2.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wordlearner2_au522133.smap_assignment2.models.Word;

import java.util.List;

@Dao
public interface WordDao {

    @Query("SELECT * FROM assignment2_au522133_wordtable ORDER BY word ASC")
    public List<Word> getAllWords();

    @Query("SELECT * FROM assignment2_au522133_wordtable WHERE word=:word")
    public Word getWord(String word);

    /*
    I have not found a "real" solution for how to avoid inserting duplicate data.
    However, I have implemented a temporary solution to ensure that the application does not crash
    when trying to enter with duplicate data using (onConflict = OnConflictStrategy.IGNORE).
    Yes, the word will though appear in the recycleview, but it will not be stored in the database, and that's the most important thing
    */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void addWord(Word word);

    @Update
    public void updateWord(Word word);

    @Delete
    public void deleteWord(Word word);

    @Query("DELETE FROM assignment2_au522133_wordtable")
    public void deleteAll();




}
