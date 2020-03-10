package com.wordlearner2_au522133.smap_assignment2.models;

import java.io.Serializable;

public class WordLearnerItem implements Serializable {
    private int mImageResource;
    private String word;
    private String pronouncing;
    private String description;
    private String rating;
    private String notes;
    private int wordPosition;

    public WordLearnerItem(int mImageResource, int wordPosition, String word, String pronouncing, String description, String rating){
        this.mImageResource = mImageResource;
        this.wordPosition = wordPosition;
        this.word = word;
        this.description = description;
        this.pronouncing = pronouncing;
        this.rating = rating;
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
