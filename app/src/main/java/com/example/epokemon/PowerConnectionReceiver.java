package com.example.epokemon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.epokemon.repository.BaseDeDadosDaApp;
import com.example.epokemon.repository.PokemonModel;
import com.example.epokemon.repository.RotinaDAO;
import com.example.epokemon.repository.RotinaDTO;
import com.example.epokemon.repository.RotinaModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PowerConnectionReceiver extends BroadcastReceiver {
    private RotinaDAO rotinaDAO;
    private PokeApiService pokeApiService;
    List<PokemonModel> pokeApiResponse;
    AdapterRecylcerViewBuy adapterRecylcerViewBuy;

//    public PowerConnectionReceiver(List<PokemonModel> pokeApiResponse, AdapterRecylcerViewBuy adapterRecylcerViewBuy) {
//        this.pokeApiResponse = pokeApiResponse;
//        this.adapterRecylcerViewBuy = adapterRecylcerViewBuy;
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Começando!!", Toast.LENGTH_LONG).show();
        pokeApiService = new PokeApiService(context);

        BaseDeDadosDaApp baseDeDadosDaApp = BaseDeDadosDaApp.getInstance(context);
        rotinaDAO = baseDeDadosDaApp.getRotinaDAO();

        List<RotinaModel> rotinaModels = rotinaDAO.listarTodos();
        System.out.println("Tamannho rodtinas models" + rotinaModels.size());
        rotinaDAO.deleteAll();
        adapterRecylcerViewBuy = (((MainActivity) context).getAdapterRecylcerViewBuy());
        pokeApiResponse = (((MainActivity) context).getPokeApiResponse());
        List<RotinaDTO> dtos = new ArrayList<>();
        try {
            for (RotinaModel rotinaModel : rotinaModels) {
                dtos.add(new RotinaDTO(rotinaModel.getId(), rotinaModel.getRating()));
            }

            pokeApiService.update(dtos);


        } catch (RuntimeException | JSONException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(
                () -> pokeApiService.fetchData(pokeApiResponse, adapterRecylcerViewBuy),
                5000);
        new Handler().postDelayed(
                () -> adapterRecylcerViewBuy.notifyDataSetChanged(),
                5000);


    }
}
