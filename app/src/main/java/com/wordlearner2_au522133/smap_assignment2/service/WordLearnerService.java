package com.wordlearner2_au522133.smap_assignment2.service;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wordlearner2_au522133.smap_assignment2.R;
import com.wordlearner2_au522133.smap_assignment2.models.Word;
import com.wordlearner2_au522133.smap_assignment2.room.WordDao;
import com.wordlearner2_au522133.smap_assignment2.room.WordRoomDatabase;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
In connection with implementing methods in WordDao, I have taken inspiration from the following yt vid:
https://www.youtube.com/watch?v=EguY40n84Xo

Besides that, I've also taken inspiration from the following demo and links to figure out how to bind to a service,
how to start a foreground service, how to send a local broadcast and how to retrieve it:
- ServiceDemo app from L5
- https://developer.android.com/guide/components/bound-services
- https://www.youtube.com/watch?v=FbpD5RZtbCc&t=27s

In addition, I've also taken inspiration from RickandMortyGallary demo (L6) to figure out how to connect properly with Volley.
Additionaly, I've also taken inspiration from https://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request
to figure out how to set header for volley request.

* */


public class WordLearnerService extends Service {

    private final IBinder binder = new LocalBinder();
    public static final String BROADCAST_BACKGROUND_SERVICE_ARRAYLIST = "com.wordlearner2_au522133.smap_assignment2.BROADCAST_BACKGROUND_SERVICE_ARRAYLIST";
    public static final String BROADCAST_BACKGROUND_SERVICE_WORD = "com.wordlearner2_au522133.smap_assignment2.BROADCAST_BACKGROUND_SERVICE_WORD";
    public static final String ARRAY_LIST = "arraylist";
    public static final String WORD_OBJECT = "word";
    public static final String TAG = "main";
    private ArrayList<Word> mWordArrayList = new ArrayList<Word>();
    private boolean runAsForegroundService = true;
    public static final String CHANNEL_ID = "exampleServiceChannel";
    private WordRoomDatabase wordRoomDatabase;
    private RequestQueue queue;
    final String API_TOKEN = "9ea49e1ccb828fd7736d981aa3b027571da9ae86";
    private NotificationManagerCompat notificationManagerCompat;
    private Handler mHandler = new Handler();
    private Random random = new Random();
    private boolean started = true;

    public class LocalBinder extends Binder {
        public WordLearnerService getService() {
            return WordLearnerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManagerCompat = NotificationManagerCompat.from(this);

        if (wordRoomDatabase == null) {
            wordRoomDatabase = Room.databaseBuilder(
                    getApplicationContext(),
                    WordRoomDatabase.class,
                    "WordDB")
                    .build();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (started) {
            Log.d(TAG, "Background service started ");

            if (runAsForegroundService) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel serviceChannel = new NotificationChannel(
                            CHANNEL_ID,
                            "WordLearnerService Channel",
                            NotificationManager.IMPORTANCE_HIGH
                    );

                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(serviceChannel);
                }


                Intent notificationIntent = new Intent(this, ListActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_announcement_black_24dp)
                        .setContentTitle("Word: null")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Eyyyy you. Even though we're in quarantine, it doesn't mean that you can just " +
                                        "chill. For this reason, I'd like to remind you to keep working hard and keep learning, as it will " +
                                        "benefit you in the end! In the case, you can start off or keep practicing this word: null "))
                        .setContentIntent(pendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .build();

                mToastRunnable.run();

                // startForeground(1, notification);
                notificationManagerCompat.notify(1, notification);

            }
        } else {
            Log.d(TAG, "Background service already started!");
        }


        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        started = false;
        Log.d(TAG, "Background service destroyed");
        super.onDestroy();
    }

    private void sendRequestWithVolley(String url) {
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        parseJson(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "That did not work!", error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + API_TOKEN);
                return map;
            }
        };

        queue.add(jsonObjectRequest);

    }

    private void parseJson(String json) {
        Gson gson = new GsonBuilder().create();
        Word word = gson.fromJson(json, Word.class);
        if (word != null) {
            mWordArrayList.add(0, word);
            word.getDefinitions().get(0).getImageUrl();

            new CreateWordsAsyncTask().execute(word);
        }
    }

    public void addWord(String word) {
        String server_url = "https://owlbot.info/api/v4/dictionary/";
        sendRequestWithVolley(server_url + word);
    }

    public void getAllWords() {
        new GetAllWordsAsyncTask().execute();
    }

    public void getWord(String nameoftheword) {
        new GetWordAsyncTask().execute(nameoftheword);
    }

    public void updateWord(String rating, String notes, int position) {
        Word word = mWordArrayList.get(position);
        word.setRating(rating);
        word.setNotes(notes);

        new UpdateWordAsyncTask().execute(word);

        mWordArrayList.set(position, word);
    }

    public void deleteWord(Word word) {
        new DeleteWordAsyncTask().execute(word);
    }

    public void deleteAllWords() {
        new DeleteAllWordsAsyncTask().execute();
    }

    public class GetAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            mWordArrayList.clear();
            mWordArrayList.addAll(wordRoomDatabase.wordDao().getAllWords());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            localBroadcastSender(mWordArrayList);
        }
    }

    public class GetWordAsyncTask extends AsyncTask<String, Void, Word> {

        @Override
        protected Word doInBackground(String... strings) {
            Word word = wordRoomDatabase.wordDao().getWord(strings[0]);
            return word;
        }

        @Override
        protected void onPostExecute(Word word) {
            super.onPostExecute(word);
            localBroadcastWordObjectSender(word);

        }
    }

    public class CreateWordsAsyncTask extends AsyncTask<Word, Void, Void> {

        @Override
        protected Void doInBackground(Word... words) {

            for (Word word : words) {
                wordRoomDatabase.wordDao().addWord(word);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            localBroadcastSender(mWordArrayList);
        }
    }

    public class UpdateWordAsyncTask extends AsyncTask<Word, Void, Void> {
        @Override
        protected Void doInBackground(Word... words) {

            wordRoomDatabase.wordDao().updateWord(words[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            localBroadcastSender(mWordArrayList);
        }
    }

    public class DeleteWordAsyncTask extends AsyncTask<Word, Void, Void> {
        @Override
        protected Void doInBackground(Word... words) {

            wordRoomDatabase.wordDao().deleteWord(words[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            localBroadcastSender(mWordArrayList);
        }
    }

    public class DeleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            wordRoomDatabase.wordDao().deleteAll();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            localBroadcastSender(mWordArrayList);
        }
    }

    private void localBroadcastSender(ArrayList<Word> words) {

        Log.d(TAG, "Using local broadcast to send arraylist ");

        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        intent.setAction(BROADCAST_BACKGROUND_SERVICE_ARRAYLIST);
        bundle.putSerializable(ARRAY_LIST, (Serializable) mWordArrayList);
        intent.putExtra("Bundle", bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void localBroadcastWordObjectSender(Word word) {
        Log.d(TAG, "Using local broadcast to send word object ");

        Intent intent = new Intent();
        intent.setAction(BROADCAST_BACKGROUND_SERVICE_WORD);
        intent.putExtra(WORD_OBJECT, word);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    /*This function delays and repeat code execution using Handler PostDelayed.
    Inspiration: https://www.youtube.com/watch?v=3pgGVBmSVq0  */
    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            // Comments to the video around 1:23-1:30: It's supposed to delay and execute it every min, but some reason
            // it should goes from lion to dog and in a few secounds and I don't really get why it does that.
            mHandler.postDelayed(this, 60000);
            Log.d(TAG, "PostDelayed test");

           /*  I know that it's not a proper loop, due to statement 2, but the app didn't really allow me to write 1,
             or even another numbers, so I found out another way to tell the app that if the size of ArrayList is 6, then it should
             only pick one of them, by subtracting with 5. Of course, it also has its consequences, as if you add a new word
             to the arraylist, then the size will be 7, and then it will pick 2 random words of the list.
             Inspiration: https://www.youtube.com/watch?v=OhStCBiiOMo
             */
            /*for (int i = 0; i < mWordArrayList.size() - 5; i++) {
                getRandomWord(mWordArrayList);
            }*/
            if(mWordArrayList.size() > 0) {

            getRandomWord(mWordArrayList);
            }
        }
    };

    private void getRandomWord(ArrayList<Word> words) {
        int index = random.nextInt(words.size());
        Log.d(TAG, "index: " + index + ", word: " + words.get(index).getWord());

        Intent notificationIntent = new Intent(this, ListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_announcement_black_24dp)
                .setContentTitle("Word: " + words.get(index).getWord())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Eyyyy you. Even though we're in quarantine, it doesn't mean that you can just " +
                                "chill. For this reason, I'd like to remind you to keep working hard and keep learning, as it will " +
                                "benefit you in the end! In the case, you can start off or keep practicing this word: " + words.get(index).getWord()))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build();

        // startForeground(1, notification);
        notificationManagerCompat.notify(1, notification);
    }
}

