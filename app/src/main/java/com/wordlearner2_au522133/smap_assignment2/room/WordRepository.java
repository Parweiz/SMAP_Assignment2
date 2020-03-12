package com.wordlearner2_au522133.smap_assignment2.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.wordlearner2_au522133.smap_assignment2.models.WordLearnerParcelable;

import java.util.List;

public class WordRepository {

    private WordDao wordDao;
    private LiveData<List<WordLearnerParcelable>> mAllWords;

    WordRepository(Application application){
        WordRoomDatabase wordRoomDatabase = WordRoomDatabase.getDatabase(application);
        wordDao = wordRoomDatabase.wordDao();
        mAllWords = wordDao.getAllWords();
    }

    LiveData<List<WordLearnerParcelable>> getAllWords() {
        return mAllWords;
    }

    void addWord(final WordLearnerParcelable w) {
        WordRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                wordDao.addWord(w);
            }
        });
    }

    public LiveData<WordLearnerParcelable> getWord(String w) {
        return wordDao.getWord(w);
    }

    void updateWord(final WordLearnerParcelable w) {
        WordRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                wordDao.updateWord(w);
            }
        });
    }

   void deleteWord(final WordLearnerParcelable w) {
        WordRoomDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                wordDao.deleteWord(w);
            }
        });
   }


}
