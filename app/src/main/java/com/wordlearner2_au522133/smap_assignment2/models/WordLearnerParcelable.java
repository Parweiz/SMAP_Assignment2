package com.wordlearner2_au522133.smap_assignment2.models;

import android.os.Parcel;
import android.os.Parcelable;

// https://stackoverflow.com/questions/12503836/how-to-save-custom-arraylist-on-android-screen-rotate
public class WordLearnerParcelable extends WordLearnerItem implements Parcelable {

    public WordLearnerParcelable(int mImageResource, int wordPosition, String word, String pronouncing, String description, String rating) {
        super(mImageResource, wordPosition, word, pronouncing, description, rating);
    }

    private WordLearnerParcelable(Parcel in){
        super(in.readInt(), in.readInt(),  in.readString(), in.readString(), in.readString(), in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getImageResource());
        dest.writeInt(getWordPosition());
        dest.writeString(getWord());
        dest.writeString(getPronouncing());
        dest.writeString(getDescription());
        dest.writeString(getRating());
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
