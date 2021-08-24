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
import android.widget.RatingBar;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.epokemon.repository.RotinaDAO;
import com.example.epokemon.repository.PokemonModel;
import com.example.epokemon.repository.RotinaModel;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerViewComprados extends RecyclerView.Adapter<CardComprados> implements Filterable {

    private List<PokemonModel> list;
    private List<PokemonModel> toSearch;
    private AdapterRecylcerViewBuy adapterRecylcerViewBuy;
    private Context ativityEmExecucao;
    private View view;
    private SearchView searchView;
    private RotinaDAO rotinaDAO;

    public AdapterRecyclerViewComprados(List<PokemonModel> valores, View view) {
        this.list = valores;
        toSearch = new ArrayList<>(list);
        this.view = view;
    }

    public RotinaDAO getPokemonDAO() {
        return rotinaDAO;
    }

    public void setPokemonDAO(RotinaDAO rotinaDAO) {
        this.rotinaDAO = rotinaDAO;
    }

    public List<PokemonModel> getToSearch() {
        return toSearch;
    }

    public void setToSearch(List<PokemonModel> toSearch) {
        this.toSearch = toSearch;
    }

    public AdapterRecylcerViewBuy getAdapterRecylcerViewBuy() {
        return adapterRecylcerViewBuy;
    }

    public void setAdapterRecylcerViewBuy(AdapterRecylcerViewBuy adapterRecylcerViewBuy) {
        this.adapterRecylcerViewBuy = adapterRecylcerViewBuy;
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

    public Filter getExampleFilter() {
        return exampleFilter;
    }

    public void setExampleFilter(Filter exampleFilter) {
        this.exampleFilter = exampleFilter;
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
        holder.price.setText("R$" + conteudoLinha.getPrice() + ",00");
        holder.hp.setText("HP: " + conteudoLinha.getHp());
        holder.attack.setText("Attack: " + conteudoLinha.getAttack());
        holder.defense.setText("Defense: " + conteudoLinha.getDefense());

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

        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RotinaModel toSave = new RotinaModel(conteudoLinha.getId(), rating);
                rotinaDAO.inserir(toSave);
                System.out.println(rotinaDAO.listarTodos());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void delete(Integer pos) {
        PokemonModel pokemonModel = list.get(pos);
        System.out.println(pokemonModel.toString());
        String url = (PokeApiService.getCrudUlr() + "/" + pokemonModel.getId());
        RequestQueue requestQueue = Volley.newRequestQueue(ativityEmExecucao.getApplicationContext());
        StringRequest dr = new StringRequest(Request.Method.DELETE, url,
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
                        Log.i("DELETE error", error.toString());

                    }
                });
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
