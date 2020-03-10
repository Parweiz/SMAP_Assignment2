package com.wordlearner2_au522133.smap_assignment2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.wordlearner2_au522133.smap_assignment2.R;

public class EditActivity extends AppCompatActivity {

    private Button cancelBtn, okBtn;
    private SeekBar seekbar;
    private TextView wordRating, txtRating, notes, txtName;
    private EditText txtNote;
    private String ratingInput, txtProgress, nameOfTheWord, notesInput, rating, note;
    private float value;
    private int valueFromSeekbar, wordPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        wordRating = (TextView) findViewById(R.id.txtEditViewWordRating);
        notes = (TextView) findViewById(R.id.txtEditNotes);
        txtNote = (EditText) findViewById(R.id.edittextEditNotesText);
        txtRating = (TextView) findViewById(R.id.txtEditRating);

        setButtons();
        setSeekbar();
        gettingDataFromDetails();
    }

    private void setSeekbar() {
        seekbar = (SeekBar) findViewById(R.id.seekBarEdit);
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

    private void setButtons() {
        cancelBtn = (Button) findViewById(R.id.cancelEditActivityBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        okBtn = (Button) findViewById(R.id.editEditActivityBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendingDataToDetails();
            }
        });
    }

    public void gettingDataFromDetails() {
        Intent intent = getIntent();
        txtName = findViewById(R.id.txtEditNameOfTheWord);

        nameOfTheWord = intent.getStringExtra(getString(R.string.key_name));
        rating = intent.getStringExtra(getString(R.string.key_rating));
        note = intent.getStringExtra(getString(R.string.key_notes));
        wordPosition = intent.getIntExtra(getString(R.string.key_position), 0);

        txtName.setText(nameOfTheWord);
        txtRating.setText(rating);
        txtNote.setText(note);

        String ratingValue = txtRating.getText().toString();
        float seekbarValue = Float.parseFloat(ratingValue) * 10;
        int seekbarProgress = (int) seekbarValue;
        seekbar.setProgress(seekbarProgress);

    }

    public void sendingDataToDetails() {
        valueFromSeekbar = seekbar.getProgress();
        float convertToDecimals = (float) valueFromSeekbar / 10;
        ratingInput = "" + convertToDecimals;

        notesInput = txtNote.getText().toString();

        Intent myIntent = new Intent();
        myIntent.putExtra(getString(R.string.key_rating), ratingInput);
        myIntent.putExtra(getString(R.string.key_notes), notesInput);
        myIntent.putExtra(getString(R.string.key_position), wordPosition);

        setResult(RESULT_OK, myIntent);
        finish();
    }

}
