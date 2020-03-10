package com.wordlearner2_au522133.smap_assignment2.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wordlearner2_au522133.smap_assignment2.R;
import com.wordlearner2_au522133.smap_assignment2.adapter.WordLearnerAdapter;
import com.wordlearner2_au522133.smap_assignment2.models.WordLearnerParcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*As I have already commented in the adapter, I have taken inspiration from the following yt vid to handle clicks
to retrieve position and then open up DetailsActivity with information about the item / object pressed. Link:
https://www.youtube.com/watch?v=WtLZK1kh-yM&feature=emb_logo

Also, in connection with orientation change, I have chosen to use Parcelable, where inspiration has been taken from:
https://stackoverflow.com/questions/12503836/how-to-save-custom-arraylist-on-android-screen-rotate

https://codinginflow.com/tutorials/android/recyclerview-edittext-search

*/
public class ListActivity extends AppCompatActivity implements WordLearnerAdapter.OnItemListener {

    private static final String TAG = "smap";

    private ArrayList<WordLearnerParcelable> mWords = new ArrayList<>();
    private WordLearnerAdapter mAdapter;
    private static final int REQUEST_CODE_DETAILS_ACTIVITY = 1;
    private String rating, note;
    private int wordClickedIndex, wordPosition, picture;
    private EditText searchTxt;


    String API_KEY = "9ea49e1ccb828fd7736d981aa3b027571da9ae86";
    String server_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        if (savedInstanceState != null) {
            mWords = savedInstanceState.getParcelableArrayList(getString(R.string.key_orientationchange));
        } else {
            mWords = new ArrayList<>();
            insertWords();
        }

        setUpExitBtn();
        setUpRecyclerView();

        searchTxt = findViewById(R.id.searchTxt);
        searchTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<WordLearnerParcelable> filteredList = new ArrayList<>();
        for (WordLearnerParcelable item : mWords) {
            if (item.getWord().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }

    public void insertWords() {
        mWords.add(new WordLearnerParcelable(R.drawable.lion, 0, "Lion", "ˈlīən", "A large tawny-coloured cat that lives in prides, found in Africa and NW India. The male has a flowing shaggy mane and takes little part in hunting, which is done cooperatively by the females.", "" + 0.0));
        mWords.add(new WordLearnerParcelable(R.drawable.leopard, 1, "Leopard", "ˈlepərd", "A large solitary cat that has a fawn or brown coat with black spots, native to the forests of Africa and southern Asia.", "" + 0.0));
        mWords.add(new WordLearnerParcelable(R.drawable.cheetah, 2, "Cheetah", "ˈCHēdə", "A large slender spotted cat found in Africa and parts of Asia. It is the fastest animal on land.", "" + 0.0));
        mWords.add(new WordLearnerParcelable(R.drawable.elephant, 3, "Elephant", "ˈeləfənt", "A very large plant-eating mammal with a prehensile trunk, long curved ivory tusks, and large ears, native to Africa and southern Asia. It is the largest living land animal.", "" + 0.0));
        mWords.add(new WordLearnerParcelable(R.drawable.giraffe, 4,"Giraffe", "jəˈraf", "A large African mammal with a very long neck and forelegs, having a coat patterned with brown patches separated by lighter lines. It is the tallest living animal.", "" + 0.0));
        mWords.add(new WordLearnerParcelable(R.drawable.kudo, 5,"Kudu", "ˈko͞odo͞o", "An African antelope that has a greyish or brownish coat with white vertical stripes, and a short bushy tail. The male has long spirally curved horns.", "" + 0.0));
        mWords.add(new WordLearnerParcelable(R.drawable.gnu, 6,"Gnu", "n(y)o͞o", "A large dark antelope with a long head, a beard and mane, and a sloping back.", "" + 0.0));
        mWords.add(new WordLearnerParcelable(R.drawable.oryx, 7,"Oryx", "null", "A large antelope living in arid regions of Africa and Arabia, having dark markings on the face and long horns.", "" + 0.0));
        mWords.add(new WordLearnerParcelable(R.drawable.camel, 8, "Camel", "ˈkaməl", "A large, long-necked ungulate mammal of arid country, with long slender legs, broad cushioned feet, and either one or two humps on the back. Camels can survive for long periods without food or drink, chiefly by using up the fat reserves in their humps.", "" + 0.0));
        mWords.add(new WordLearnerParcelable(R.drawable.shark, 9, "Shark", "SHärk", "A long-bodied chiefly marine fish with a cartilaginous skeleton, a prominent dorsal fin, and tooth-like scales. Most sharks are predatory, though the largest kinds feed on plankton, and some can grow to a large size.", "" + 0.0));
        mWords.add(new WordLearnerParcelable(R.drawable.crocodile, 10,"Crocodile", "ˈkräkəˌdīl", "A large predatory semiaquatic reptile with long jaws, long tail, short legs, and a horny textured skin.", "" + 0.0));
        mWords.add(new WordLearnerParcelable(R.drawable.snake, 11, "Snake", "snāk", "A long limbless reptile which has no eyelids, a short tail, and jaws that are capable of considerable extension. Some snakes have a venomous bite.", "" + 0.0));
        mWords.add(new WordLearnerParcelable(R.drawable.buffalo, 12, "Buffalo", "ˈbəf(ə)ˌlō", "A heavily built wild ox with backward-curving horns, found mainly in the Old World tropics:", "" + 0.0));
        mWords.add(new WordLearnerParcelable(R.drawable.ostrich, 13, "Ostrich", "ˈästriCH", "A flightless swift-running African bird with a long neck, long legs, and two toes on each foot. It is the largest living bird, with males reaching a height of up to 2.75 m.", "" + 0.0));
    }

    public void setUpRecyclerView() {
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new WordLearnerAdapter(mWords, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        WordLearnerParcelable clickedWord = mWords.get(position);
        wordClickedIndex = position;

        intent.putExtra(getString(R.string.key_picture), clickedWord.getImageResource());
        intent.putExtra(getString(R.string.key_name), clickedWord.getWord());
        intent.putExtra(getString(R.string.key_pronouncing), clickedWord.getPronouncing());
        intent.putExtra(getString(R.string.key_description), clickedWord.getDescription());
        intent.putExtra(getString(R.string.key_rating), clickedWord.getRating());
        intent.putExtra(getString(R.string.key_notes), clickedWord.getNotes());
        intent.putExtra(getString(R.string.key_position), clickedWord.getWordPosition());

        startActivityForResult(intent, REQUEST_CODE_DETAILS_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DETAILS_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    rating = data.getStringExtra(getString(R.string.key_rating));
                    note = data.getStringExtra(getString(R.string.key_notes));
                    wordPosition = data.getIntExtra(getString(R.string.key_position), 0);

                   /* As I'm not sure if I should write that I received help from the lecturers or not,
                   I choose to be on the safe side and announce that I have been assisted by Kasper
                   in updating the recycler view when data arrives .*/
                    WordLearnerParcelable i = mWords.get(wordPosition);
                    i.setRating(rating);
                    i.setWordPosition(wordPosition);

                    mWords.set(wordPosition, i);
                    mAdapter.updateData(mWords);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void setUpExitBtn() {
        Button exitBtn = (Button) findViewById(R.id.exitBtn);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(getString(R.string.key_orientationchange), mWords);
        super.onSaveInstanceState(outState);
    }

    private void httpRequestWithVolley() {
        // Initialize a new RequestQueue instance
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                server_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Process the JSON
                        try {

                            JSONArray jsonArray = response.getJSONArray("definitions");
                            // Loop through the array elements
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String definition = jsonObject.getString("definition");
                                String image = jsonObject.getString("image_url");
                                picture = Integer.parseInt(image);

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
                        error.printStackTrace();
                    }
                }


        );
        requestQueue.add(jsonObjectRequest);
    }





}
