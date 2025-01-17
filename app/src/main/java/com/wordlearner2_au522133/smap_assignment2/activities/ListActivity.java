package com.wordlearner2_au522133.smap_assignment2.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.facebook.stetho.Stetho;
import com.wordlearner2_au522133.smap_assignment2.R;
import com.wordlearner2_au522133.smap_assignment2.adapter.WordLearnerAdapter;
import com.wordlearner2_au522133.smap_assignment2.models.Word;
import com.wordlearner2_au522133.smap_assignment2.room.WordRoomDatabase;
import com.wordlearner2_au522133.smap_assignment2.service.WordLearnerService;

import java.util.ArrayList;

import static com.wordlearner2_au522133.smap_assignment2.service.WordLearnerService.ARRAY_LIST;

/*As I have already commented in the adapter, I have taken inspiration from the following yt vid to handle clicks
to retrieve position and then open up DetailsActivity with information about the item / object pressed. Link:
https://www.youtube.com/watch?v=WtLZK1kh-yM&feature=emb_logo

Even though there's not any requirements about you should be able to delete all words, I found it necessary to implement that
Inspiration:
https://google-developer-training.github.io/android-developer-advanced-course-practicals/unit-6-working-with-architecture-components/lesson-14-room,-livedata,-viewmodel/14-1-b-room-delete-data/14-1-b-room-delete-data.html#task2intro

*/
public class ListActivity extends AppCompatActivity implements WordLearnerAdapter.OnItemListener {

    private static final int REQUEST_CODE_DETAILS_ACTIVITY = 1;
    private int wordClickedIndex;
    private ArrayList<Word> mWords = new ArrayList<>();
    private EditText editText;
    private WordLearnerAdapter mAdapter;
    public static final String TAG = "wordlearnerservice";
    private RecyclerView mRecyclerView;
    WordLearnerService wordLearnerService = new WordLearnerService();
    private ServiceConnection boundService;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        setUpRecyclerView();
        enableStethos();
        boundServiceSetupFunction();

    }

    @Override
    protected void onStart() {
        super.onStart();
        startingAndBindingToTheService();

        IntentFilter filter = new IntentFilter();
        filter.addAction(WordLearnerService.BROADCAST_BACKGROUND_SERVICE_ARRAYLIST);
        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadcastReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        unBindingFromTheSerivce();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcastReceiver);
    }

    private void startingAndBindingToTheService() {
        Log.d(TAG, "Registering receivers and binding to the service");
        Intent intent = new Intent(this, WordLearnerService.class);
        startService(intent);
        bindService(intent, boundService, Context.BIND_AUTO_CREATE);
    }

    private void unBindingFromTheSerivce() {
        Log.d(TAG, "Unregistering receivers and unbinding to the service");

        if (mBound) {
            unbindService(boundService);
            mBound = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Look at comments above the class to understand why I have chosen to implement code to delete all words
        int id = item.getItemId();

        if (id == R.id.clear_all_data) {
            wordLearnerService.deleteAllWords();
            wordLearnerService.getAllWords();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setUpRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new WordLearnerAdapter(mWords, this, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void exitBtn(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void addBtn(View v) {
        editText = findViewById(R.id.searchTxt);
        String word = editText.getText().toString();

        wordLearnerService.addWord(word);
    }

    private void boundServiceSetupFunction() {
        boundService = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {

                WordLearnerService.LocalBinder binder = (WordLearnerService.LocalBinder) service;
                wordLearnerService = binder.getService();
                mBound = true;
                Log.d(TAG, "Boundservice connected - ListActivity");

                wordLearnerService.getAllWords();
            }

            public void onServiceDisconnected(ComponentName className) {
                wordLearnerService = null;
                mBound = false;
                Log.d(TAG, "Boundservice disconnected - ListActivity");
            }
        };
    }

    private BroadcastReceiver localBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Broadcast received from the service");

            Bundle bundle = intent.getBundleExtra("Bundle");
            mWords = (ArrayList<Word>) bundle.getSerializable(ARRAY_LIST);

            mAdapter.updateData(mWords);
        }
    };

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        Word clickedWord = mWords.get(position);
        Log.d(TAG, "onItemClick: " + clickedWord);
        wordClickedIndex = position;

        intent.putExtra(getString(R.string.key_name), clickedWord.getWord());
        intent.putExtra(getString(R.string.key_position), wordClickedIndex);

        startActivityForResult(intent, REQUEST_CODE_DETAILS_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DETAILS_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                }
            }
        }
    }

    // Taken from TheSituationRoom demo from L4
    private void enableStethos() {
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(
                        Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(
                        Stetho.defaultInspectorModulesProvider(this))
                .build());
    }
}
