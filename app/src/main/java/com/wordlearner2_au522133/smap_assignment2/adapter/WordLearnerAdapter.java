package com.wordlearner2_au522133.smap_assignment2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wordlearner2_au522133.smap_assignment2.R;
import com.wordlearner2_au522133.smap_assignment2.models.WordLearnerParcelable;

import java.util.ArrayList;
import java.util.List;

/*In connection with the implementation of the adapter and the implementation of code for handling clicks in recyclerview,
so that we can move on to DetailsActivity, inspiration has been taken in the following yt vid:
https://www.youtube.com/watch?v=WtLZK1kh-yM&feature=emb_logo
*/

public class WordLearnerAdapter extends RecyclerView.Adapter<WordLearnerAdapter.WordViewHolder> {
    private ArrayList<WordLearnerParcelable> mWordList;
    private OnItemListener mOnItemListener;
    private List<WordLearnerParcelable> mWords;

    public void filterList(ArrayList<WordLearnerParcelable> filteredList) {
        mWordList = filteredList;
        notifyDataSetChanged();
    }


    public class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mPicture;
        TextView txtName, txtPronouncing, txtRating;
        OnItemListener onItemListener;

        public WordViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);

            mPicture = itemView.findViewById(R.id.picture);
            txtName = itemView.findViewById(R.id.txtNameOfTheWord);
            txtPronouncing = itemView.findViewById(R.id.txtPronoucing);
            txtRating = itemView.findViewById(R.id.txtListRating);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public WordLearnerAdapter(ArrayList<WordLearnerParcelable> wordList, OnItemListener onItemListener) {
        this.mWordList = wordList;
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
        WordLearnerParcelable word = mWordList.get(position);

        int image = word.getImageResource();
        String name = word.getWord();
        String pronounce = word.getPronouncing();
        String rating = word.getRating();

        holder.mPicture.setImageResource(image);
        holder.txtName.setText(name);
        holder.txtPronouncing.setText(pronounce);
        holder.txtRating.setText(rating);


    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }


    public void setWords(List<WordLearnerParcelable> words) {
        mWords = words;
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<WordLearnerParcelable> newList ){
        mWordList = newList;
    }

}
