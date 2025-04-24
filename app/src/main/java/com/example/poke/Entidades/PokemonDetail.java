package com.example.poke.Entidades;

import java.util.List;

public class PokemonDetail {
    private int id;
    private String name;
    private Sprites sprites;
    private List<Type> types;

    public PokemonDetail(int id, String name, Sprites sprites, List<Type> types) {
        this.id = id;
        this.name = name;
        this.sprites = sprites;
        this.types = types;
    }

    public PokemonDetail() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }
}
