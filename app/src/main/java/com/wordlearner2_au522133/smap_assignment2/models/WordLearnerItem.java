package com.wordlearner2_au522133.smap_assignment2.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "wordlearner2_word_table")
public class WordLearnerItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "Picture")
    private int mImageResource;
    @ColumnInfo(name = "Word Position")
    private int wordPosition;
    @ColumnInfo(name = "Name of the word")
    private String word;
    @ColumnInfo(name = "Pronunciation")
    private String pronouncing;
    @ColumnInfo(name = "Description")
    private String description;
    @ColumnInfo(name = "Rating")
    private String rating;
    @ColumnInfo(name = "Notes")
    private String notes;

    public WordLearnerItem(int mImageResource, int wordPosition, String word, String pronouncing, String description, String rating){
        this.mImageResource = mImageResource;
        this.wordPosition = wordPosition;
        this.word = word;
        this.description = description;
        this.pronouncing = pronouncing;
        this.rating = rating;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }


    public int getImageResource() {
        return mImageResource;
    }

    public void setImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


    public String getPronouncing() {
        return pronouncing;
    }

    public void setPronouncing(String pronouncing) {
        this.pronouncing = pronouncing;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public int getWordPosition() {
        return wordPosition;
    }

    public void setWordPosition(int wordPosition) {
        this.wordPosition = wordPosition;
    }
}
