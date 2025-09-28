package com.example.dungeon.model;

public class Potion extends Item {
    private final int heal;

    public Potion(String name, int heal) {
        super(name);
        this.heal = heal;
    }

    @Override
    public void apply(GameState ctx) {
        Player p = ctx.getPlayer();
        int newHp = Math.min(p.getHp() + heal, p.getMaxHp());
        int actualHeal = newHp - p.getHp();
        p.setHp(newHp);

        int cost = 5;
        int availablePoints = Math.min(cost, ctx.getScore());
        ctx.addScore(-availablePoints);

        System.out.println("Выпито зелье: +" + actualHeal + " HP. Текущее HP: " + p.getHp());
        if (availablePoints > 0) {
            System.out.println("Списано " + availablePoints + " очков. Текущий счет: " + ctx.getScore());
        } else {
            System.out.println("Очки не списаны (недостаточно очков). Текущий счет: " + ctx.getScore());
        }
        p.getInventory().remove(this);
    }
}