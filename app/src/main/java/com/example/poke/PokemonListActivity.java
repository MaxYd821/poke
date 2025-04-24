package com.example.poke;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poke.Adapter.PokemonAdapter;
import com.example.poke.Entidades.Pokemon;
import com.example.poke.Entidades.PokemonList;
import com.example.poke.Services.PokemonService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PokemonListActivity extends AppCompatActivity {

    RecyclerView rvPokemon;
    boolean isLoading = false;
    boolean isLastPage = false;
    int currentPage = 1;
    Pokemon pokemon;
    List<PokemonList> data = new ArrayList<>();
    PokemonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pokemon_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pokemon), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvPokemon = findViewById(R.id.rvPokemon);
        rvPokemon.setLayoutManager(new LinearLayoutManager(this));

        setUpRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(getApplicationContext(), "ColorActivity onResume", Toast.LENGTH_SHORT).show();

        data.clear();
        currentPage = 1;
        isLastPage = false;
        adapter.notifyDataSetChanged();

        loadMorePokemons();
    }

    private void loadMorePokemons(){

        isLoading = true;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        PokemonService service = retrofit.create(PokemonService.class);
        int offset = (currentPage - 1) * 20;
        service.getPokemon(20,offset).enqueue(new Callback<Pokemon>() {

            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                isLoading = false;
                if (!response.isSuccessful() || response.body() == null) return;

                List<PokemonList> results = response.body().getResults();

                if (results.isEmpty()) {
                    isLastPage = true;
                    return;
                }

                data.addAll(results);
                adapter.notifyDataSetChanged();
                //List<ColorClass> data = response.body();

            }

            @Override
            public void onFailure(Call<Pokemon> call, Throwable throwable) {
                isLoading = false;
            }
        });
    }

    private void setUpRecyclerView() {

        adapter = new PokemonAdapter(data);
        rvPokemon.setAdapter(adapter);

        rvPokemon.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {

                        currentPage++;
                        loadMorePokemons();
                    }
                }
            }
        });
    }
}