package com.wordlearner2_au522133.smap_assignment2.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.wordlearner2_au522133.smap_assignment2.models.WordLearnerParcelable;

import java.util.List;

public class WordViewModel extends AndroidViewModel {
    private WordRepository mWordRepository;

    private LiveData<List<WordLearnerParcelable>> mAllWords;

    private WordViewModel(@NonNull Application application) {
        super(application);
        mWordRepository = new WordRepository(application);
        mAllWords = mWordRepository.getAllWords();
    }

    public LiveData<List<WordLearnerParcelable>> getAllWords() {
        return mAllWords;
    }

    public LiveData<WordLearnerParcelable> getWord(String w) {
        return mWordRepository.getWord(w);
    }

    public void addWord(WordLearnerParcelable w) {
        mWordRepository.addWord(w);
    }

    public void updateWord(WordLearnerParcelable w) {
        mWordRepository.updateWord(w);
    }

    public void deleteWord(WordLearnerParcelable w) {
        mWordRepository.deleteWord(w);
    }
}
