package com.example.poke;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.poke.Entidades.Ubicacion;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FormUbicacionActivity extends AppCompatActivity {

    EditText etLatitud, etLongitud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_ubicacion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);

        String pokemonId = getIntent().getStringExtra("pokemonId");

        findViewById(R.id.btnGuardar).setOnClickListener(v -> {
            Ubicacion ubicacion = new Ubicacion();
            ubicacion.latitud = etLatitud.getText().toString();
            ubicacion.longitud = etLongitud.getText().toString();
            ubicacion.pokemonId = pokemonId;

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            DatabaseReference ubicacionRef = myRef.child("ubicaciones").push();
            ubicacion.idUbicacion = ubicacionRef.getKey();
            ubicacionRef.setValue(ubicacion)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getApplicationContext(), "Ubicación guardada", Toast.LENGTH_LONG).show();
                    }).addOnFailureListener(f -> {
                        Toast.makeText(getApplicationContext(), "Error al guardar ubicación", Toast.LENGTH_LONG).show();
                    });
        });
    }
}