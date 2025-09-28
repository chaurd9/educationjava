package com.example.dungeon.model;

import java.util.*;

public class Room {
    private final String name;
    private final String description;
    private final Map<String, Room> neighbors = new HashMap<>();
    private final List<Item> items = new ArrayList<>();
    private Monster monster;
    private final List<Item> chest = new ArrayList<>();
    private final Map<String, Boolean> lockedExits = new HashMap<>();
    private final Map<String, String> exitKeys = new HashMap<>();

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public Map<String, Room> getNeighbors() {
        return neighbors;
    }

    public List<Item> getItems() {
        return items;
    }

    public Monster getMonster() {
        return monster;
    }

    public List<Item> getChest() { return chest; }

    public void setMonster(Monster m) {
        this.monster = m;
    }

    public String describe() {
        StringBuilder sb = new StringBuilder(name + ": " + description);
        if (!items.isEmpty()) {
            sb.append("\nПредметы: ").append(String.join(", ", items.stream().map(Item::getName).toList()));
        }
        if (monster != null) {
            sb.append("\nВ комнате монстр: ").append(monster.getName()).append(" (ур. ").append(monster.getLevel()).append(")");
        }
        if (chest.isEmpty()) {
            sb.append("\nСундук: пуст");
        } else {
            sb.append("\nВ сундуке: ").append(String.join(", ", chest.stream().map(Item::getName).toList()));
        }
        if (!neighbors.isEmpty()) {
            sb.append("\nВыходы: ").append(String.join(", ", neighbors.keySet()));
        }
        return sb.toString();
    }

    public Map<String, Boolean> getLockedExits() {
        return lockedExits;
    }

    public Map<String, String> getExitKeys() {
        return exitKeys;
    }

    public boolean isExitLocked(String direction) {
        return lockedExits.getOrDefault(direction, false);
    }

    public void lockExit(String direction, String keyName) {
        lockedExits.put(direction, true);
        exitKeys.put(direction, keyName);
    }

    public void unlockExit(String direction) {
        lockedExits.put(direction, false);
        exitKeys.remove(direction);
    }

    public String getExitKeyName(String direction) {
        return exitKeys.get(direction);
    }
}