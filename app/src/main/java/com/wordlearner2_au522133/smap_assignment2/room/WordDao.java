package com.wordlearner2_au522133.smap_assignment2.room;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wordlearner2_au522133.smap_assignment2.models.WordLearnerParcelable;

import java.util.List;

public interface WordDao {

    @Insert
    void addWord(WordLearnerParcelable w);

    @Query("SELECT * FROM wordlearner2_word_table")
    LiveData<List<WordLearnerParcelable>> getAllWords();

    @Query("SELECT * from wordlearner2_word_table ORDER BY `Name of the word` ASC")
    LiveData<List<WordLearnerParcelable>> getAlphabetizedWords();


    @Query("SELECT * FROM wordlearner2_word_table WHERE uid=:u")
    LiveData<WordLearnerParcelable> getWord(String u);

    @Update
    void updateWord(WordLearnerParcelable w);

    @Delete
    void deleteWord(WordLearnerParcelable w);
}
