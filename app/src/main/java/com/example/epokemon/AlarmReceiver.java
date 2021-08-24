package com.example.epokemon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.epokemon.repository.BaseDeDadosDaApp;
import com.example.epokemon.repository.RotinaDAO;
import com.example.epokemon.repository.RotinaDTO;
import com.example.epokemon.repository.RotinaModel;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    private RotinaDAO rotinaDAO;
    private PokeApiService pokeApiService;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        BaseDeDadosDaApp baseDeDadosDaApp = BaseDeDadosDaApp.getInstance(context);
        rotinaDAO = baseDeDadosDaApp.getRotinaDAO();

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        if (level > 15) {
            List<RotinaModel> base = rotinaDAO.listarTodos();

            pokeApiService = new PokeApiService(context);
            List<RotinaDTO> rotinaDTOS = new ArrayList<>();
            for (RotinaModel rotina: base){
                rotinaDTOS.add(new RotinaDTO(rotina.getId(), rotina.getRating()));
            }
            try {
                pokeApiService.update(rotinaDTOS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



}
