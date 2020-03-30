package com.wordlearner2_au522133.smap_assignment2.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.wordlearner2_au522133.smap_assignment2.R;
import com.wordlearner2_au522133.smap_assignment2.adapter.WordLearnerAdapter;
import com.wordlearner2_au522133.smap_assignment2.models.Word;
import com.wordlearner2_au522133.smap_assignment2.service.WordLearnerService;

import java.io.Serializable;
import java.util.ArrayList;

import static com.wordlearner2_au522133.smap_assignment2.service.WordLearnerService.ARRAY_LIST;
import static com.wordlearner2_au522133.smap_assignment2.service.WordLearnerService.WORD_OBJECT;

/*As I have already commented in the adapter, I have taken inspiration from the following yt vid to handle clicks
to retrieve position and then open up DetailsActivity with information about the item / object pressed. Link:
https://www.youtube.com/watch?v=WtLZK1kh-yM&feature=emb_logo

Sadly, the app crashes when you switch to landscape mode meanwhile you're at DetailsActivity.
It returns with a NullPointerException, and I can't really figure out how to fix it.
For that reason, I've decided to let it be for now and fix it after peer review.
*/

public class DetailsActivity extends AppCompatActivity {

    private Button cancelBtn, editBtn, deleteBtn;
    private String word, pronunciation, rating, note, definition;
    private Object mPicture;
    private TextView txtName, txtPronunciation, txtNote, txtRating, txtDefinition;
    private ImageView imgWord;
    private static final int REQUEST_CODE_EDIT_ACTIVITY = 2;
    WordLearnerService wordLearnerService;
    private ServiceConnection boundService;
    private boolean mBound = false;
    public static final String TAG = "activity";
    private Word wordObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        imgWord = findViewById(R.id.mDetailsPicture);
        txtName = findViewById(R.id.mDetailsTxtName);
        txtPronunciation = findViewById(R.id.txtDetailsPronouncing);
        txtDefinition = findViewById(R.id.txtDetailsDefinitionText);
        txtRating = findViewById(R.id.txtDetailsRating);
        txtNote = findViewById(R.id.txtDetailsNotesText);

        setDetailsBtns();
        boundServiceFunc();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, WordLearnerService.class);
        bindService(intent, boundService, Context.BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(WordLearnerService.BROADCAST_BACKGROUND_SERVICE_WORD);

        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mBound) {
            unbindService(boundService);
            mBound = false;
        }

        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcastReceiver);
    }

    private void boundServiceFunc() {
        boundService = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                WordLearnerService.LocalBinder binder = (WordLearnerService.LocalBinder) service;
                wordLearnerService = binder.getService();

                word = getIntent().getStringExtra(getString(R.string.key_name));
                wordLearnerService.getWord(word);

                mBound = true;
                Log.d(TAG, "Boundservice connected - DetailsActivity");

            }

            public void onServiceDisconnected(ComponentName className) {
                wordLearnerService = null;
                mBound = false;
                Log.d(TAG, "Boundservice disconnected - DetailsActivity");
            }
        };
    }

    private void setDetailsBtns() {
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
                Log.d(TAG, "Deleting: " + wordObject.getWord());
                wordLearnerService.deleteWord(wordObject);
                finish();
            }
        });
    }

    private BroadcastReceiver localBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            wordObject = (Word) intent.getSerializableExtra(WORD_OBJECT);
            Log.d(TAG, "onReceive: " + wordObject);
            setDataAfterBroadcast();
        }
    };

    private void setDataAfterBroadcast() {
        mPicture = wordObject.getDefinitions().get(0).getImageUrl();
        Log.d(TAG, "setDataAfterBroadcast: " + mPicture);
        word = wordObject.getWord();
        pronunciation = wordObject.getPronunciation();
        definition = wordObject.getDefinitions().get(0).getDefinition();
        rating = wordObject.getRating();
        note = wordObject.getNotes();

        if (mPicture != null) {
            Glide.with(imgWord.getContext()).load(mPicture)
                    .into(imgWord);
        } else {
            Glide.with(imgWord.getContext()).load(R.drawable.coffee_default_image)
                    .into(imgWord);
        }

        txtName.setText(word);

        if (pronunciation != null) {
            txtPronunciation.setText(pronunciation);
        } else {
            txtPronunciation.setText("null");
        }

        txtDefinition.setText(definition);

        if (rating != null) {
            txtRating.setText(rating);
        } else {
            txtRating.setText("0.0");
        }

        txtNote.setText(note);
    }

    public void sendingDataToEditActivity() {
        Intent i = new Intent(this, EditActivity.class);
        i.putExtra(getString(R.string.key_name), word);
        i.putExtra(getString(R.string.key_rating), rating);
        i.putExtra(getString(R.string.key_notes), note);
        i.putExtra(getString(R.string.key_position), getIntent().getIntExtra(getString(R.string.key_position), 0));

        startActivityForResult(i, REQUEST_CODE_EDIT_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        }
    }
}
