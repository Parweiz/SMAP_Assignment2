package com.wordlearner2_au522133.smap_assignment2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wordlearner2_au522133.smap_assignment2.R;
import com.wordlearner2_au522133.smap_assignment2.models.Word;

import java.util.ArrayList;

/*In connection with the implementation of the adapter and the implementation of code for handling clicks in recyclerview,
so that we can move on to DetailsActivity, inspiration has been taken in the following yt vid:
https://www.youtube.com/watch?v=WtLZK1kh-yM&feature=emb_logo
*/

public class WordLearnerAdapter extends RecyclerView.Adapter<WordLearnerAdapter.WordViewHolder> {
    private ArrayList<Word> mWordArrayList;
    private OnItemListener mOnItemListener;
    //private List<Word> mWords;

    public void filterList(ArrayList<Word> filteredList) {
        mWordArrayList = filteredList;
        notifyDataSetChanged();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imgWord;
        TextView txtName, txtPronunciation, txtRating;
        OnItemListener onItemListener;

        public WordViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            imgWord = itemView.findViewById(R.id.picture);
            txtName = itemView.findViewById(R.id.txtNameOfTheWord);
            txtPronunciation = itemView.findViewById(R.id.txtPronoucing);
            txtRating = itemView.findViewById(R.id.txtListRating);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }

        public void setWords(ArrayList<Word> words) {
            mWordArrayList = words;
            notifyDataSetChanged();
        }
    }

    public Word getWordAtPosition(int position) {
        return mWordArrayList.get(position);
    }

    public WordLearnerAdapter(ArrayList<Word> wordList, Context c, OnItemListener onItemListener) {
        this.mWordArrayList = wordList;
        this.mOnItemListener = onItemListener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordlearner_item, parent, false);
        WordViewHolder wvh = new WordViewHolder(v, mOnItemListener);
        return wvh;
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {


        Object mPicture = mWordArrayList.get(position)
                .getDefinitions()
                .get(0)
                .getImageUrl();

        if (mPicture != null) {
            Glide.with(holder.imgWord.getContext()).load(mPicture)
                    .into(holder.imgWord);
        } else {
            Glide.with(holder.imgWord.getContext()).load(R.drawable.coffee_default_image)
                    .into(holder.imgWord);
        }

        holder.txtName.setText(mWordArrayList.get(position).getWord());

        String pronounciation = mWordArrayList.get(position).getPronunciation();
        if (pronounciation != null) {
            holder.txtPronunciation.setText(pronounciation);
        } else {
            holder.txtPronunciation.setText("null");
        }



        String rating = mWordArrayList.get(position).getRating();
        if (rating != null) {
            holder.txtRating.setText(rating);
        } else {
            holder.txtRating.setText("0.0");
        }

    }

    @Override
    public int getItemCount() {
        if (mWordArrayList != null) {
            return mWordArrayList.size();
        } else return 0;
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }

    public interface OnDeleteClickListener {
        void OnDeleteClickListener(Word myWord);
    }


    public void updateData(ArrayList<Word> newList) {
        mWordArrayList = newList;
    }

}
