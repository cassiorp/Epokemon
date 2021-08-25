package com.example.epokemon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.epokemon.repository.PokemonModel;
import com.example.epokemon.repository.RotinaDTO;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PokeApiService {

    private Context context;
    //private static final String FETHC_POKE_API_ULR = "http://172.28.0.1:8000/v1/pokeapi";
    private static final String FETHC_POKE_API_ULR = "http://pokeapi-env.eba-zambya2i.us-east-2.elasticbeanstalk.com/v1/pokeapi";
    //private static final String CRUD_ULR = "http://172.28.0.1:8000/v1/store";
    private static final String CRUD_ULR = "http://pokeapi-env.eba-zambya2i.us-east-2.elasticbeanstalk.com/v1/store";

    public PokeApiService(Context context) {
        this.context = context;
    }


    public void fetchData(List<PokemonModel> models, AdapterRecylcerViewBuy adapterRecylcerViewBuy) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FETHC_POKE_API_ULR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("RESPONSE FETCH DATA", response);
                            models.clear();
                            models.addAll(mountData(response));
                            adapterRecylcerViewBuy.setList(models);
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

    public void fetchComprados(List<PokemonModel> models, AdapterRecyclerViewComprados adapterRecyclerViewComprados) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, CRUD_ULR,
                new Response.Listener<String>() {
                    @SuppressLint("LongLogTag")
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("RESPONSE FETCH COMPRADOS", response);
                            models.addAll(mountData(response));
                            models.stream().forEach(m -> System.out.println(m.getId() + "\n" + m.getName()));
                            adapterRecyclerViewComprados.setList(models);
                            adapterRecyclerViewComprados.notifyDataSetChanged();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void update(List<RotinaDTO> rotinas) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonBody = new JSONObject();
        for (RotinaDTO rotina : rotinas) {
            try {
                jsonBody.put("id", rotina.getId());
                jsonBody.put("rating", rotina.getRating());
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, FETHC_POKE_API_ULR, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Response do upadate rotina", String.valueOf(response));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });
            queue.add(jsonObjectRequest);
        }

    }


    public List<PokemonModel> mountData(String response) throws JSONException {
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

    public static String getCrudUlr() {
        return CRUD_ULR;
    }
}
