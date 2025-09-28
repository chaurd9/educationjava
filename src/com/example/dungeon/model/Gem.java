package com.example.dungeon.model;

public class Gem extends Item {
    public Gem(String name) {
        super(name);
    }

    @Override
    public void apply(GameState ctx) {
        System.out.println("Драгоценный камень сверкает в ваших руках. Это ценный трофей!");
    }
}