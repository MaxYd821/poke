package com.example.poke;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.poke.Entidades.PokemonDetail;
import com.example.poke.Services.PokemonService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetallepokActivity extends AppCompatActivity {

    TextView tvName, tvTipo1, tvTipo2;
    ImageView ivPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detallepok);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detallepokemon), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvName = findViewById(R.id.tvName);
        tvTipo1 = findViewById(R.id.tvTipo1);
        tvTipo2 = findViewById(R.id.tvTipo2);
        ivPokemon = findViewById(R.id.ivPokemon);

        String name = getIntent().getStringExtra("name");
        String url = getIntent().getStringExtra("url");

        int id = extractIdFromUrl(url);
        fetchPokemonDetails(id);
    }

    private int extractIdFromUrl(String url) {
        String[] parts = url.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }

    private void fetchPokemonDetails(int id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PokemonService service = retrofit.create(PokemonService.class);
        service.getPokemonDetail(id).enqueue(new Callback<PokemonDetail>() {
            @Override
            public void onResponse(Call<PokemonDetail> call, Response<PokemonDetail> response) {
                if (!response.isSuccessful() || response.body() == null) return;

                PokemonDetail pokemon = response.body();
                tvName.setText(pokemon.getName());

                if(pokemon.getTypes().size() > 0){
                    tvTipo1.setText(pokemon.getTypes().get(0).getType().getName());
                }
                if (pokemon.getTypes().size() > 1) {
                    tvTipo2.setText(pokemon.getTypes().get(1).getType().getName());
                }

                String imageUrl = pokemon.getSprites().getFront_default();
                Log.d("ImageURL", "URL: " + imageUrl);
                Glide.with(DetallepokActivity.this).load(imageUrl).into(ivPokemon);
            }

            @Override
            public void onFailure(Call<PokemonDetail> call, Throwable t) {
                // Handle failure
            }
        });
    }
}