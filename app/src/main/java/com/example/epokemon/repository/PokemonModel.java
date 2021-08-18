package com.example.epokemon.repository;

public class PokemonModel {
    private String id;
    private String image;
    private String name;
    private String price;
    private String hp;
    private String attack;
    private String defense;

    public PokemonModel(String image, String name, String price, String hp, String attack, String defense) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
    }

    public PokemonModel(String id, String image, String name, String price, String hp, String attack, String defense) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getAttack() {
        return attack;
    }

    public void setAttack(String attack) {
        this.attack = attack;
    }

    public String getDefense() {
        return defense;
    }

    public void setDefense(String defense) {
        this.defense = defense;
    }
}
