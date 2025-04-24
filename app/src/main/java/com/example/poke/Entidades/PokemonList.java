package com.example.poke.Entidades;

public class PokemonList {
    private String name;
    private String url;

    public PokemonList(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public PokemonList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}