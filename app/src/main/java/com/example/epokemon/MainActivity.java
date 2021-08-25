package com.example.epokemon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.example.epokemon.repository.BaseDeDadosDaApp;
import com.example.epokemon.repository.RotinaDAO;
import com.example.epokemon.repository.PokemonModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.example.epokemon.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private PokeApiService pokeApiService;
    private ActivityMainBinding binding;

    private List<PokemonModel> pokeApiResponse;
    private List<PokemonModel> comprados;

    private AdapterRecyclerViewComprados adaptadorDoRecyclerViewComprados;
    private AdapterRecylcerViewBuy adapterRecylcerViewBuy;

    private SearchView searchView;
    private FloatingActionButton floatingActionButton;

    private Integer RECOGNIZER_RESULT = 1;

    private View viewHome;
    private View viewStore;

    private RotinaDAO rotinaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pokeApiResponse = new ArrayList<>();
        comprados = new ArrayList<>();

        adaptadorDoRecyclerViewComprados = new AdapterRecyclerViewComprados();
        adapterRecylcerViewBuy = new AdapterRecylcerViewBuy();

        pokeApiService = new PokeApiService(getApplicationContext());

        viewHome = findViewById(R.id.navigation_home);
        viewStore = findViewById(R.id.navigation_store);

        searchView = findViewById(R.id.searchView);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);


        BaseDeDadosDaApp db = BaseDeDadosDaApp.getInstance(this);
        rotinaDAO = db.getRotinaDAO();

        adaptadorDoRecyclerViewComprados.setPokemonDAO(rotinaDAO);

        new Thread(new Runnable() {
            @Override
            public void run() {
                pokeApiService.fetchComprados(comprados, adaptadorDoRecyclerViewComprados);
                pokeApiService.fetchData(pokeApiResponse, adapterRecylcerViewBuy);
                adapterRecylcerViewBuy.setView(viewStore);
                adapterRecylcerViewBuy.notifyDataSetChanged();
                adaptadorDoRecyclerViewComprados.setView(viewHome);
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
                searchIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-br");
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
                //adaptadorDoRecyclerViewComprados.getFilter().filter(newText);
                adapterRecylcerViewBuy.getFilter().filter(newText);
                return false;
            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mShakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                if (!pokeApiResponse.isEmpty()) {
                    List<PokemonModel> copy = new ArrayList<>(pokeApiResponse);
                    Collections.shuffle(copy);
                    searchView.setQuery(copy.get(0).getName(), true);
                }
            }
        });

        BroadcastReceiver receiverPowerAdapter = new PowerConnectionReceiver();
        IntentFilter filterPowerAdapter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        this.registerReceiver(receiverPowerAdapter, filterPowerAdapter);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_store).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
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

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    public ShakeDetector getmShakeDetector() {
        return mShakeDetector;
    }

    public void setmShakeDetector(ShakeDetector mShakeDetector) {
        this.mShakeDetector = mShakeDetector;
    }

    public SensorManager getmSensorManager() {
        return mSensorManager;
    }

    public void setmSensorManager(SensorManager mSensorManager) {
        this.mSensorManager = mSensorManager;
    }

    public Sensor getmAccelerometer() {
        return mAccelerometer;
    }

    public void setmAccelerometer(Sensor mAccelerometer) {
        this.mAccelerometer = mAccelerometer;
    }

    public ActivityMainBinding getBinding() {
        return binding;
    }

    public void setBinding(ActivityMainBinding binding) {
        this.binding = binding;
    }

    public List<PokemonModel> getPokeApiResponse() {
        return pokeApiResponse;
    }

    public void setPokeApiResponse(List<PokemonModel> pokeApiResponse) {
        this.pokeApiResponse = pokeApiResponse;
    }

    public void setComprados(List<PokemonModel> comprados) {
        this.comprados = comprados;
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }

    public FloatingActionButton getFloatingActionButton() {
        return floatingActionButton;
    }

    public void setFloatingActionButton(FloatingActionButton floatingActionButton) {
        this.floatingActionButton = floatingActionButton;
    }

    public AdapterRecyclerViewComprados getAdaptadorDoRecyclerViewComprados() {
        return adaptadorDoRecyclerViewComprados;
    }

    public void setAdaptadorDoRecyclerViewComprados(AdapterRecyclerViewComprados adaptadorDoRecyclerViewComprados) {
        this.adaptadorDoRecyclerViewComprados = adaptadorDoRecyclerViewComprados;
    }

    public AdapterRecylcerViewBuy getAdapterRecylcerViewBuy() {
        return adapterRecylcerViewBuy;
    }

    public void setAdapterRecylcerViewBuy(AdapterRecylcerViewBuy adapterRecylcerViewBuy) {
        this.adapterRecylcerViewBuy = adapterRecylcerViewBuy;
    }

    public Integer getRECOGNIZER_RESULT() {
        return RECOGNIZER_RESULT;
    }

    public void setRECOGNIZER_RESULT(Integer RECOGNIZER_RESULT) {
        this.RECOGNIZER_RESULT = RECOGNIZER_RESULT;
    }

    public View getViewHome() {
        return viewHome;
    }

    public void setViewHome(View viewHome) {
        this.viewHome = viewHome;
    }

    public View getViewStore() {
        return viewStore;
    }

    public void setViewStore(View viewStore) {
        this.viewStore = viewStore;
    }

    public List<PokemonModel> getComprados() {
        return comprados;
    }

    public PokeApiService getPokeApiService() {
        return pokeApiService;
    }

    public void setPokeApiService(PokeApiService pokeApiService) {
        this.pokeApiService = pokeApiService;
    }

    public RotinaDAO getRotinaDAO() {
        return rotinaDAO;
    }

    public void setRotinaDAO(RotinaDAO rotinaDAO) {
        this.rotinaDAO = rotinaDAO;
    }
}