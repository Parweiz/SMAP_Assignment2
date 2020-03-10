package com.wordlearner2_au522133.smap_assignment2.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wordlearner2_au522133.smap_assignment2.R;
import com.wordlearner2_au522133.smap_assignment2.adapter.WordLearnerAdapter;
import com.wordlearner2_au522133.smap_assignment2.models.WordLearnerParcelable;

import java.util.ArrayList;


/*As I have already commented in the adapter, I have taken inspiration from the following yt vid to handle clicks
to retrieve position and then open up DetailsActivity with information about the item / object pressed. Link:
https://www.youtube.com/watch?v=WtLZK1kh-yM&feature=emb_logo

*/

public class DetailsActivity extends AppCompatActivity {

    private ArrayList<WordLearnerParcelable> mWords = new ArrayList<WordLearnerParcelable>();
    private WordLearnerAdapter mAdapter;
    private Button cancelBtn, editBtn, deleteBtn;
    private String nameOfTheWord, pronoucing, description, rating, note;
    private int picture, positionId, wordItemPosition ;
    private TextView txtName, txtPronouncing, txtDescription, txtNote, txtRating;
    private ImageView mPicture;
    private static final int REQUEST_CODE_EDIT_ACTIVITY = 2;

    private static final String TAG = "smap";

    public DetailsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mPicture = findViewById(R.id.mDetailsPicture);
        txtName = findViewById(R.id.mDetailsTxtName);
        txtPronouncing = findViewById(R.id.txtDetailsPronouncing);
        txtDescription = findViewById(R.id.txtDetailsDescriptionText);
        txtRating = findViewById(R.id.txtDetailsRating);
        txtNote = findViewById(R.id.txtDetailsNotesText);

        setButtons();
        gettingDataFromList();

        if (savedInstanceState != null) {
            mWords = savedInstanceState.getParcelableArrayList(getString(R.string.key_orientationchange));
            wordItemPosition = savedInstanceState.getInt(getString(R.string.key_position));
        }
    }


    public void setButtons() {
        cancelBtn = (Button) findViewById(R.id.cancelDetailsActivityBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        editBtn = (Button) findViewById(R.id.editDetailsActivityBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendingDataToEditActivity();
            }
        });

        deleteBtn = (Button) findViewById(R.id.deleteDetailsActivityBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = wordItemPosition;
                removeWord(position);
                finish();
            }
        });

    }



    public void removeWord(int position) {
        Toast.makeText(this, "Position" + position, Toast.LENGTH_SHORT).show();
        mWords.remove(position);
        mAdapter.notifyItemChanged(position);
    }

    public void gettingDataFromList() {
        Intent intent = getIntent();

        picture = intent.getIntExtra(getString(R.string.key_picture), 0);
        nameOfTheWord = intent.getStringExtra(getString(R.string.key_name));
        pronoucing = intent.getStringExtra(getString(R.string.key_pronouncing));
        description = intent.getStringExtra(getString(R.string.key_description));
        rating = intent.getStringExtra(getString(R.string.key_rating));
        note = intent.getStringExtra(getString(R.string.key_notes));

        positionId = intent.getIntExtra("pos", 0);
        Log.d(TAG, "gettingDataFromList: " + positionId);

        wordItemPosition = intent.getIntExtra(getString(R.string.key_position), 0);
        Toast.makeText(this, "Position: " + wordItemPosition, Toast.LENGTH_SHORT).show();

        mWords =  (ArrayList<WordLearnerParcelable>) getIntent().getParcelableExtra("mylist");

        mPicture.setImageResource(picture);
        txtName.setText(nameOfTheWord);
        txtPronouncing.setText(pronoucing);
        txtDescription.setText(description);
        txtRating.setText(rating);
        txtNote.setText(note);
    }

    public void sendingDataToEditActivity() {
        Intent i = new Intent(this, EditActivity.class);
        i.putExtra(getString(R.string.key_name), nameOfTheWord);
        i.putExtra(getString(R.string.key_rating), rating);
        i.putExtra(getString(R.string.key_notes), note);
        i.putExtra(getString(R.string.key_position), wordItemPosition);

        startActivityForResult(i, REQUEST_CODE_EDIT_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    note = data.getStringExtra(getString(R.string.key_notes));
                    rating = data.getStringExtra(getString(R.string.key_rating));

                    txtNote.setText(note);
                    txtRating.setText(rating);

                    setResult(RESULT_OK, data);
                    finish();
                }

            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.key_orientationchange), mWords);
        outState.putInt(getString(R.string.key_position), wordItemPosition);
        super.onSaveInstanceState(outState);
    }
}
