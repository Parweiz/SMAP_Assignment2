package com.wordlearner2_au522133.smap_assignment2.service;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wordlearner2_au522133.smap_assignment2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class WordLearnerService extends Service {

    // Binder given to clients - This is the object that receives interactions from clients.
    private final IBinder binder = new LocalBinder();

    private boolean runAsForegroundService = true;


    public static final String CHANNEL_ID = "exampleServiceChannel";
    public static final String LOG = "service";

    // For web api
    String server_url = "https://owlbot.info/api/v4/dictionary/owl";
    final String API_TOKEN = "9ea49e1ccb828fd7736d981aa3b027571da9ae86";

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public WordLearnerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return WordLearnerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        if (runAsForegroundService) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel serviceChannel = new NotificationChannel(
                        CHANNEL_ID,
                        "WordLearnerService Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                );

                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(serviceChannel);
            }


            Intent notificationIntent = new Intent(this, ListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("WordLearnerService")
                    .setContentText(input)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(1, notification);
        } else {
            Log.d(LOG, "Background service onStartCommand - already started!");
        }

        // Tell the user we started.
        Toast.makeText(this, "WordLearnerService started", Toast.LENGTH_LONG).show();

        httpRequestWithVolley();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Tell the user we stopped.
        Toast.makeText(this, "WordLearnerService stopped", Toast.LENGTH_SHORT).show();
    }

    private void httpRequestWithVolley() {
        // Initialize a new RequestQueue instance
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Initialize a new JsonArrayRequest instance
        final JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                server_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Process the JSON
                        try {
                            // Display the data (json object) in json object
                            String json = response.toString();
                            Log.d(LOG, "onResponse: " + response);
                            JSONArray jsonArray = response.getJSONArray("definitions");

                            // Loop through the array elements
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String definition = jsonObject.getString("definition");
                                String image = jsonObject.getString("image_url");
                            }

                            String word = response.getString("word");
                            String pronunciation = response.getString("pronunciation");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //  return super.getHeaders();

                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + API_TOKEN);
                return map;
            }
        };

        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);


    }

}
