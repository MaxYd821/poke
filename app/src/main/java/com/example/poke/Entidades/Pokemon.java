package com.example.poke.Entidades;

import java.util.List;

public class Pokemon {
    private List<PokemonList> results;
    public List<Ubicacion> ubicaciones;

    public Pokemon(List<PokemonList> results) {
        this.results = results;
    }

    public Pokemon() {
    }

    public List<PokemonList> getResults() {
        return results;
    }
    public void setResults(List<PokemonList> results) {
        this.results = results;
    }
}
