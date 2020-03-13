package com.wordlearner2_au522133.smap_assignment2.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// https://stackoverflow.com/questions/12503836/how-to-save-custom-arraylist-on-android-screen-rotate

@Entity(tableName = "wordlearner2_word_table")
public class WordLearnerParcelable implements Parcelable {

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



    public WordLearnerParcelable(int mImageResource, int wordPosition, String word, String pronouncing, String description, String rating) {
        // super(mImageResource, wordPosition, word, pronouncing, description, rating);
        this.mImageResource = mImageResource;
        this.wordPosition = wordPosition;
        this.word = word;
        this.pronouncing = pronouncing;
        this.description = description;
        this.rating = rating;

    }


    private WordLearnerParcelable(Parcel in){
        // super(in.readInt(), in.readInt(),  in.readString(), in.readString(), in.readString(), in.readString());
        mImageResource = in.readInt();
        wordPosition = in.readInt();
        word = in.readString();
        pronouncing = in.readString();
        description = in.readString();
        rating = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mImageResource);
        dest.writeInt(wordPosition);
        dest.writeString(word);
        dest.writeString(pronouncing);
        dest.writeString(description);
        dest.writeString(rating);
    }

    public static final Creator<WordLearnerParcelable> CREATOR = new Creator<WordLearnerParcelable>() {
        @Override
        public WordLearnerParcelable createFromParcel(Parcel in) {
            return new WordLearnerParcelable(in);
        }

        @Override
        public WordLearnerParcelable[] newArray(int size) {
            return new WordLearnerParcelable[size];
        }
    };

}
