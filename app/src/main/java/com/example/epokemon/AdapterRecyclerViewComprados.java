package com.example.epokemon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.epokemon.repository.PokemonModel;
import com.example.epokemon.ui.home.HomeFragment;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerViewComprados extends RecyclerView.Adapter<CardComprados> implements Filterable {

    private List<PokemonModel> list;
    private List<PokemonModel> toSearch;
    private Context ativityEmExecucao;
    private View view;
    private SearchView searchView;

    public AdapterRecyclerViewComprados(List<PokemonModel> valores, View view) {
        this.list = valores;
        toSearch = new ArrayList<>(list);
        this.view = view;
    }

    public AdapterRecyclerViewComprados() {
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public void setList(List<PokemonModel> list) {
        this.list = list;
        this.toSearch = new ArrayList<>(list);
    }

    public void setView(View view) {
        this.view = view;
    }

    public List<PokemonModel> getList() {
        return list;
    }

    @NonNull
    @NotNull
    @Override
    public CardComprados onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ativityEmExecucao = parent.getContext();
        final CardComprados holder = new CardComprados(LayoutInflater.from(ativityEmExecucao)
                .inflate(R.layout.card_view_comprados, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CardComprados holder, int position) {
        PokemonModel conteudoLinha = list.get(position);
        String imageUri = conteudoLinha.getImage();
        Picasso.with(ativityEmExecucao.getApplicationContext()).load(imageUri).into(holder.image);
        holder.name.setText("Nome: " + String.valueOf(conteudoLinha.getName()));
        holder.price.setText("Preço R$"+conteudoLinha.getPrice() + ",00");
        holder.hp.setText("HP: " +conteudoLinha.getHp());
        holder.attack.setText("Attack: " +conteudoLinha.getAttack());
        holder.defense.setText("Defense: " +conteudoLinha.getDefense());

        holder.buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete(position);
                Log.i("Delete", "delete");
                delete(position);
                notifyDataSetChanged();
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

    public void delete(Integer pos) {
        PokemonModel pokemonModel = list.get(pos);
        System.out.println(pokemonModel.getId());
        String deleteUrl = "http://pokeapi-env.eba-zambya2i.us-east-2.elasticbeanstalk.com/v1/store/" + pokemonModel.getId();
        RequestQueue requestQueue = Volley.newRequestQueue(ativityEmExecucao.getApplicationContext());
        StringRequest dr = new StringRequest(Request.Method.DELETE, deleteUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Snackbar.make(view, R.string.text_delete, Snackbar.LENGTH_SHORT)
                                .setMaxInlineActionWidth(100)
                                .show();
                        Log.i("DELETE", response);
                        list.remove(pokemonModel);
                        notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.
                        Log.i("DELETE error", error.toString());

                    }
                }
        );
        requestQueue.add(dr);
        notifyDataSetChanged();
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
            System.out.println(filteredList.size());

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
