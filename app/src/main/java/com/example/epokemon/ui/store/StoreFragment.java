package com.example.epokemon.ui.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epokemon.AdapterRecyclerViewComprados;
import com.example.epokemon.AdapterRecylcerViewBuy;
import com.example.epokemon.MainActivity;
import com.example.epokemon.R;
import com.example.epokemon.repository.PokemonModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class StoreFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private AdapterRecylcerViewBuy adaptadorDoRecyclerView;
    private AdapterRecyclerViewComprados adapterRecyclerViewComprados;
    private List<PokemonModel> pokemonModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.store_notifications, container, false);

        pokemonModels = (((MainActivity) getActivity()).pokeApiResponse);

        adapterRecyclerViewComprados = (((MainActivity) getActivity()).adaptadorDoRecyclerViewComprados);

        adaptadorDoRecyclerView =  (((MainActivity) getActivity()).adapterRecylcerViewBuy);

        adaptadorDoRecyclerView.setAdapterRecyclerViewComprados(adapterRecyclerViewComprados);

        recyclerView = view.findViewById(R.id.recyclerViewBuy);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adaptadorDoRecyclerView);

        return view;
    }


}