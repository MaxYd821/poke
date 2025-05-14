package com.example.poke;

import androidx.fragment.app.FragmentActivity;

import androidx.annotation.NonNull;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.poke.Entidades.Ubicacion;
import com.example.poke.Utils.PermissionUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.poke.databinding.ActivityMapsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private Location lastLocation;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String pokemonId = getIntent().getStringExtra("pokemonId");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ubicacionesRef = database.getReference("ubicaciones");

        ubicacionesRef.orderByChild("pokemonId").equalTo(pokemonId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ubicacionSnapshot : snapshot.getChildren()) {
                            Ubicacion ubicacion = ubicacionSnapshot.getValue(Ubicacion.class);
                            if (ubicacion != null) {
                                LatLng position = new LatLng(
                                        Double.parseDouble(ubicacion.latitud),
                                        Double.parseDouble(ubicacion.longitud)
                                );
                                mMap.addMarker(new MarkerOptions().position(position).title("Ubicación"));
                            }
                        }
                        if (mMap != null && snapshot.getChildrenCount() > 0) {
                            // Centrar el mapa en la primera ubicación
                            DataSnapshot first = snapshot.getChildren().iterator().next();
                            Ubicacion firstUbicacion = first.getValue(Ubicacion.class);
                            if (firstUbicacion != null) {
                                LatLng firstPosition = new LatLng(
                                        Double.parseDouble(firstUbicacion.latitud),
                                        Double.parseDouble(firstUbicacion.longitud)
                                );
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstPosition, 15));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MapsActivity.this, "Error al cargar ubicaciones", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if (lastLocation != null) {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();

            LatLng userLocation = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(userLocation).title("Tu ubicación"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        } else {
            // Maneja el caso en que la ubicación es null
            Toast.makeText(this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
        }
    }
}