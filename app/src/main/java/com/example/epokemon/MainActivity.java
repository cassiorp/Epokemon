package com.example.epokemon;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.epokemon.repository.PokemonModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.epokemon.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public List<PokemonModel> pokeApiResponse = new ArrayList<>();
    public List<PokemonModel> comprados = new ArrayList<>();
    public SearchView searchView;
    public FloatingActionButton floatingActionButton;
    public AdapterRecyclerViewComprados adaptadorDoRecyclerViewComprados;
    public AdapterRecylcerViewBuy adapterRecylcerViewBuy;
    public Integer RECOGNIZER_RESULT = 1;
    public View viewHome;
    public View viewStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewHome = findViewById(R.id.navigation_home);
        viewStore = findViewById(R.id.navigation_store);

        searchView = findViewById(R.id.searchView);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        adaptadorDoRecyclerViewComprados = new AdapterRecyclerViewComprados();
        adapterRecylcerViewBuy = new AdapterRecylcerViewBuy();


        new Thread(new Runnable() {
            @Override
            public void run() {
                getComprados();
                fetchData();
                adaptadorDoRecyclerViewComprados.setView(viewHome);
                adapterRecylcerViewBuy.setView(viewStore);
                adapterRecylcerViewBuy.notifyDataSetChanged();
                adaptadorDoRecyclerViewComprados.notifyDataSetChanged();
            }
        }).start();

        floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                searchIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                searchIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Procure");
                startActivityForResult(searchIntent, RECOGNIZER_RESULT);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adaptadorDoRecyclerViewComprados.getFilter().filter(newText);
                adapterRecylcerViewBuy.getFilter().filter(newText);
                return false;
            }
        });


        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_store)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);;
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        navView.setSelectedItemId(R.id.navigation_store);
        navView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        if (requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.i("VOZ", matches.get(0));
            searchView.setQuery(matches.get(0), true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://pokeapi-env.eba-zambya2i.us-east-2.elasticbeanstalk.com/v1/pokeapi";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pokeApiResponse.addAll(mountData(response));
                            adapterRecylcerViewBuy.setList(pokeApiResponse);
                            adapterRecylcerViewBuy.notifyDataSetChanged();
                        } catch (JSONException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERRO RESPONSE", String.valueOf(error));
            }
        });
        queue.add(stringRequest);
    }

    private List<PokemonModel> mountData(String response) throws JSONException {
        Gson gson = new Gson();
        List<PokemonModel> responses = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            PokemonModel model = gson.fromJson(String.valueOf(jsonObject), PokemonModel.class);
            responses.add(model);
        }
        return responses;
    }


    public void getComprados() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://pokeapi-env.eba-zambya2i.us-east-2.elasticbeanstalk.com/v1/store";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            comprados.addAll(mountData(response));
                            adaptadorDoRecyclerViewComprados.setList(comprados);
                            adaptadorDoRecyclerViewComprados.notifyDataSetChanged();
                        } catch (JSONException e) {
                            throw new RuntimeException(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERRO RESPONSE", String.valueOf(error));
            }
        });
        queue.add(stringRequest);
    }
}