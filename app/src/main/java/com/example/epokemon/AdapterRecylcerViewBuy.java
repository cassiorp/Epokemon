package com.example.epokemon;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.epokemon.repository.PokemonModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecylcerViewBuy extends RecyclerView.Adapter<CardModelBuy> implements Filterable {
    // valores
    private List<PokemonModel> list;
    private List<PokemonModel> toSearch;
    public AdapterRecyclerViewComprados adapterRecyclerViewComprados;
    private Context ativityEmExecucao;
    private View view;

    public AdapterRecylcerViewBuy(List<PokemonModel> valores,
                                  AdapterRecyclerViewComprados adapterRecyclerViewComprados,
                                  View view) {
        this.list = valores;
        toSearch = new ArrayList<>(list);
        this.adapterRecyclerViewComprados = adapterRecyclerViewComprados;
        this.view = view;
    }

    public AdapterRecylcerViewBuy() {
    }

    public List<PokemonModel> getList() {
        return list;
    }


    public void setList(List<PokemonModel> list) {
        this.list = list;
        this.toSearch = new ArrayList<>(list);
    }

    public List<PokemonModel> getToSearch() {
        return toSearch;
    }

    public void setToSearch(List<PokemonModel> toSearch) {
        this.toSearch = toSearch;
    }

    public AdapterRecyclerViewComprados getAdapterRecyclerViewComprados() {
        return adapterRecyclerViewComprados;
    }

    public void setAdapterRecyclerViewComprados(AdapterRecyclerViewComprados adapterRecyclerViewComprados) {
        this.adapterRecyclerViewComprados = adapterRecyclerViewComprados;
    }

    public Context getAtivityEmExecucao() {
        return ativityEmExecucao;
    }

    public void setAtivityEmExecucao(Context ativityEmExecucao) {
        this.ativityEmExecucao = ativityEmExecucao;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Filter getExampleFilter() {
        return exampleFilter;
    }

    public void setExampleFilter(Filter exampleFilter) {
        this.exampleFilter = exampleFilter;
    }

    @NonNull
    @NotNull
    @Override
    public CardModelBuy onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ativityEmExecucao = parent.getContext();
        final CardModelBuy holder = new CardModelBuy(LayoutInflater.from(ativityEmExecucao)
                .inflate(R.layout.card_view_buy, parent, false));

        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolder(@NonNull @NotNull CardModelBuy holder, int position) {

        PokemonModel pokemonHolder = list.get(position);

        String imageUri = pokemonHolder.getImage();

        Picasso.with(ativityEmExecucao.getApplicationContext()).load(imageUri).into(holder.image);

        holder.name.setText("Nome: " + String.valueOf(pokemonHolder.getName()));
        holder.price.setText("R$" + pokemonHolder.getPrice() + ",00");
        holder.hp.setText("HP: " + pokemonHolder.getHp());
        holder.attack.setText("Attack: " + pokemonHolder.getAttack());
        holder.defense.setText("Defense: " + pokemonHolder.getDefense());
        holder.ratingBar.setRating(pokemonHolder.getRating());

        holder.buttonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy(pokemonHolder);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(1200);
                rotate.setInterpolator(new LinearInterpolator());
                holder.image.startAnimation(rotate);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void buy(PokemonModel pokemonModel) {
        String postUrl = PokeApiService.getCrudUlr();
        RequestQueue requestQueue = Volley.newRequestQueue(ativityEmExecucao.getApplicationContext());
        JSONObject postData = new JSONObject();
        Gson gson = new Gson();
        try {
            postData.put("id", pokemonModel.getId());
            postData.put("name", pokemonModel.getName());
            postData.put("image", pokemonModel.getImage());
            postData.put("price", pokemonModel.getPrice());
            postData.put("hp", pokemonModel.getHp());
            postData.put("attack", pokemonModel.getAttack());
            postData.put("defense", pokemonModel.getDefense());
            postData.put("rating", pokemonModel.getRating());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Response POST", String.valueOf(response));
                        Snackbar.make(view, R.string.text_label, Snackbar.LENGTH_SHORT)
                                .show();
                        PokemonModel added = gson.fromJson(String.valueOf(response), PokemonModel.class);
                        adapterRecyclerViewComprados.getList().add(added);
                        adapterRecyclerViewComprados.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar.make(view, R.string.text_error_buy, Snackbar.LENGTH_SHORT)
                        .show();
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PokemonModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(toSearch);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PokemonModel item : toSearch) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list.clear();
            list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
