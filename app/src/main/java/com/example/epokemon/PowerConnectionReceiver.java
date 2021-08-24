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
        rotinaDAO.deleteAll();
        adapterRecylcerViewBuy = (((MainActivity) context).getAdapterRecylcerViewBuy());
        pokeApiResponse = (((MainActivity) context).getPokeApiResponse());
        try {
            for (RotinaModel rotinaModel : rotinaModels) {
                RotinaDTO dto = new RotinaDTO(rotinaModel.getId(), rotinaModel.getRating());
                System.out.println(dto.getId());
                pokeApiService.update(dto);
            }


//                new Handler().postDelayed(
//                        () -> pokeApiService.fetchData(pokeApiResponse, adapterRecylcerViewBuy),
//                        2000);
//                new Handler().postDelayed(
//                        () -> adapterRecylcerViewBuy.notifyDataSetChanged(),
//                        1000);

        } catch (RuntimeException | JSONException e) {
            e.printStackTrace();
        }

    }
}
