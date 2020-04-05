package com.wordlearner2_au522133.smap_assignment2.activities;

import androidx.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wordlearner2_au522133.smap_assignment2.R;
import com.wordlearner2_au522133.smap_assignment2.models.Word;
import com.wordlearner2_au522133.smap_assignment2.service.WordLearnerService;

import java.util.ArrayList;

import static com.wordlearner2_au522133.smap_assignment2.service.WordLearnerService.WORD_OBJECT;

public class EditActivity extends AppCompatActivity {

    private Button cancelBtn, updateBtn;
    private SeekBar seekbar;
    private TextView txtRating, txtName;
    private EditText txtNote;
    private String ratingInput, txtProgress, word, notesInput, rating, note;
    private float value;
    private int valueFromSeekbar, wordPosition;
    public static final String TAG = "wordlearner2";
    WordLearnerService wordLearnerService = new WordLearnerService();
    private ServiceConnection boundService;
    private boolean mBound = false;
    private ArrayList<Word> mWords = new ArrayList<>();
    private Word wordObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        txtName = findViewById(R.id.txtEditNameOfTheWord);
        txtNote = findViewById(R.id.edittextEditNotesText);
        txtRating = findViewById(R.id.txtEditRating);

        setSeekbar();
       //  gettingDataFromDetails();
        boundServiceFunc();
        setEditBtns();
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindingToTheService();

        IntentFilter filter = new IntentFilter();
        filter.addAction(WordLearnerService.BROADCAST_BACKGROUND_SERVICE_WORD);
        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindingFromTheService();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcastReceiver);
    }

    private void bindingToTheService() {
        Log.d(TAG, "Binding to the service from EditActivity ");
        Intent intent = new Intent(this, WordLearnerService.class);
        bindService(intent, boundService, Context.BIND_AUTO_CREATE);
    }

    private void unbindingFromTheService() {
        Log.d(TAG, "Unbinding to the service from EditActivity ");
        if (mBound) {
            unbindService(boundService);
            mBound = false;
        }
    }

    private void boundServiceFunc() {
        boundService = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                WordLearnerService.LocalBinder binder = (WordLearnerService.LocalBinder) service;
                wordLearnerService = binder.getService();
                mBound = true;
                Log.d(TAG, "Boundservice connected - EditActivity");

                word = getIntent().getStringExtra(getString(R.string.key_name));
                wordPosition = getIntent().getIntExtra(getString(R.string.key_position), 0);
                wordLearnerService.getWord(word);
            }

            public void onServiceDisconnected(ComponentName className) {
                wordLearnerService = null;
                mBound = false;
                Log.d(TAG, "Boundservice disconnected - EditActivity");
            }
        };
    }

    private BroadcastReceiver localBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            wordObject = (Word) intent.getSerializableExtra(WORD_OBJECT);
            setDataAfterBroadcast();
        }
    };

    private void setDataAfterBroadcast() {
        word = wordObject.getWord();
        rating = wordObject.getRating();
        note = wordObject.getNotes();

        txtName.setText(word);
        txtNote.setText(note);

        if (rating != null) {
            txtRating.setText(rating);
            String ratingValue = txtRating.getText().toString();
            float seekbarValue = Float.parseFloat(ratingValue) * 10;
            int seekbarProgress = (int) seekbarValue;
            seekbar.setProgress(seekbarProgress);
        } else {
            txtRating.setText("0.0");
        }

    }

    private void setSeekbar() {
        seekbar = findViewById(R.id.seekBarEdit);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value = (float) progress / 10;
                txtProgress = Float.toString(value);
                txtRating.setText(txtProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setEditBtns() {
        cancelBtn = findViewById(R.id.cancelEditActivityBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        updateBtn = findViewById(R.id.updateEditActivityBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent();
                valueFromSeekbar = seekbar.getProgress();
                float convertToDecimals = (float) valueFromSeekbar / 10;
                ratingInput = "" + convertToDecimals;

                notesInput = txtNote.getText().toString();

                wordLearnerService.updateWord(ratingInput, notesInput, wordPosition);
                setResult(RESULT_OK, myIntent);
                finish();
            }
        });
    }

    public void gettingDataFromDetails() {
        Intent intent = getIntent();

        word = intent.getStringExtra(getString(R.string.key_name));
        rating = intent.getStringExtra(getString(R.string.key_rating));
        note = intent.getStringExtra(getString(R.string.key_notes));
        wordPosition = intent.getIntExtra(getString(R.string.key_position), 0);

        txtName.setText(word);
        txtRating.setText(rating);
        txtNote.setText(note);

        if (rating != null) {
            String ratingValue = txtRating.getText().toString();
            float seekbarValue = Float.parseFloat(ratingValue) * 10;
            int seekbarProgress = (int) seekbarValue;
            seekbar.setProgress(seekbarProgress);
        }
    }


}
