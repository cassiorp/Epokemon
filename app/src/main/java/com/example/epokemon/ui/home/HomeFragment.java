package com.example.epokemon.ui.home;

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
import com.example.epokemon.MainActivity;
import com.example.epokemon.R;
import com.example.epokemon.databinding.FragmentHomeBinding;
import com.example.epokemon.repository.PokemonModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private RecyclerView recyclerView;
    public AdapterRecyclerViewComprados adaptadorDoRecyclerView;
    public SearchView searchView;
    private FloatingActionButton floatingActionButton;
    private List<PokemonModel> comprados;
    public Integer RECOGNIZER_RESULT = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        comprados = (((MainActivity) getActivity()).comprados);
        adaptadorDoRecyclerView = (((MainActivity) getActivity()).adaptadorDoRecyclerViewComprados);
        recyclerView = root.findViewById(R.id.recyclerViewComprados);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adaptadorDoRecyclerView);
        floatingActionButton = (((MainActivity) getActivity()).floatingActionButton);
        return root;
    }


}
