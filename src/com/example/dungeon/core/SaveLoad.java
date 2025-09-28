package com.example.dungeon.core;

import com.example.dungeon.model.*;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SaveLoad {
    private static final Path SAVE = Paths.get("save.txt");
    private static final Path SCORES = Paths.get("scores.csv");

    public static void save(GameState s) {
        try (BufferedWriter w = Files.newBufferedWriter(SAVE)) {
            Player p = s.getPlayer();
            w.write("player;" + p.getName() + ";" + p.getHp() + ";" + p.getMaxHp() + ";" + p.getAttack());
            w.newLine();

            String inv = p.getInventory().stream().map(i -> i.getClass().getSimpleName() + ":" + i.getName()).collect(Collectors.joining(","));
            w.write("inventory;" + inv);
            w.newLine();

            w.write("room;" + s.getCurrent().getName());
            w.newLine();

            for (Room r : s.getRooms().values()) {
                String itemsStr = r.getItems().stream().map(i -> i.getClass().getSimpleName() + ":" + i.getName()).collect(Collectors.joining(","));
                String monsterStr = r.getMonster() != null ? r.getMonster().getName() + ";" + r.getMonster().getLevel() + ";" + r.getMonster().getHp() : "";
                String chestStr = r.getChest().stream().map(i -> i.getClass().getSimpleName() + ":" + i.getName()).collect(Collectors.joining(","));
                StringBuilder lockedExitsStr = new StringBuilder();
                for (Map.Entry<String, Boolean> entry : r.getLockedExits().entrySet()) {
                    if (entry.getValue()) {
                        String keyName = r.getExitKeyName(entry.getKey());
                        if (keyName != null) {
                            if (lockedExitsStr.length() > 0) lockedExitsStr.append(",");
                            lockedExitsStr.append(entry.getKey()).append(":").append(keyName);
                        }
                    }
                }

                w.write("room_state;" + r.getName() + ";items:" + itemsStr + ";monster:" + monsterStr +
                        ";chest:" + chestStr + ";locked_exits:" + lockedExitsStr);
                w.newLine();
            }

            System.out.println("Сохранено в " + SAVE.toAbsolutePath());
            writeScore(p.getName(), s.getScore());
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось сохранить игру", e);
        }
    }

    public static void load(GameState s) {
        if (!Files.exists(SAVE)) {
            System.out.println("Сохранение не найдено.");
            return;
        }
        try (BufferedReader r = Files.newBufferedReader(SAVE)) {
            Map<String, String> map = new HashMap<>();
            for (String line; (line = r.readLine()) != null; ) {
                String[] parts = line.split(";", 2);
                if (parts.length == 2) map.put(parts[0], parts[1]);
            }

            Player p = s.getPlayer();
            String playerData = map.get("player");
            if (playerData != null) {
                String[] pp = playerData.split(";");
                if (pp.length >= 5) {
                    p.setName(pp[1]);
                    p.setHp(Integer.parseInt(pp[2]));
                    p.setMaxHp(Integer.parseInt(pp[3]));
                    p.setAttack(Integer.parseInt(pp[4]));
                }
            }

            p.getInventory().clear();
            String inv = map.getOrDefault("inventory", "");
            if (!inv.isBlank()) {
                for (String tok : inv.split(",")) {
                    String[] t = tok.split(":", 2);
                    if (t.length < 2) continue;
                    switch (t[0]) {
                        case "Potion" -> p.getInventory().add(new Potion(t[1], 5));
                        case "Key" -> p.getInventory().add(new Key(t[1]));
                        case "Weapon" -> p.getInventory().add(new Weapon(t[1], 3));
                        default -> {}
                    }
                }
            }

            String roomName = map.getOrDefault("room", "Площадь");
            Room loadedRoom = s.getRooms().get(roomName);
            if (loadedRoom != null) {
                s.setCurrent(loadedRoom);
            }

            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey().equals("room_state")) {
                    String[] parts = entry.getValue().split(";");
                    String rName = parts[0];
                    Room room = s.getRooms().get(rName);
                    if (room == null) continue;

                    room.getItems().clear();
                    room.setMonster(null);
                    room.getChest().clear();
                    room.getLockedExits().clear();
                    room.getExitKeys().clear();

                    for (int i = 1; i < parts.length; i++) {
                        if (parts[i].startsWith("items:")) {
                            String itemsStr = parts[i].substring(6);
                            addItemsFromString(room.getItems(), itemsStr);
                        } else if (parts[i].startsWith("monster:")) {
                            String monsterStr = parts[i].substring(8);
                            if (!monsterStr.isEmpty()) {
                                String[] mParts = monsterStr.split(";");
                                if (mParts.length == 3) {
                                    room.setMonster(new Monster(mParts[0], Integer.parseInt(mParts[1]), Integer.parseInt(mParts[2])));
                                }
                            }
                        } else if (parts[i].startsWith("chest:")) {
                            String chestStr = parts[i].substring(6);
                            addItemsFromString(room.getChest(), chestStr);
                        } else if (parts[i].startsWith("locked_exits:")) {
                            String lockedExitsStr = parts[i].substring(13);
                            if (!lockedExitsStr.isEmpty()) {
                                for (String exitInfo : lockedExitsStr.split(",")) {
                                    if (!exitInfo.isEmpty()) {
                                        String[] exitParts = exitInfo.split(":");
                                        if (exitParts.length == 2) {
                                            room.lockExit(exitParts[0], exitParts[1]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            System.out.println("Игра загружена.");
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось загрузить игру", e);
        }
    }

    private static void addItemsFromString(List<Item> list, String str) {
        if (str.isBlank()) return;
        for (String tok : str.split(",")) {
            String[] t = tok.split(":", 2);
            if (t.length < 2) continue;
            switch (t[0]) {
                case "Potion" -> list.add(new Potion(t[1], 5));
                case "Key" -> list.add(new Key(t[1]));
                case "Weapon" -> list.add(new Weapon(t[1], 3));
                default -> {}
            }
        }
    }

    public static void printScores() {
        if (!Files.exists(SCORES)) {
            System.out.println("Пока нет результатов.");
            return;
        }
        try (BufferedReader r = Files.newBufferedReader(SCORES)) {
            System.out.println("Таблица лидеров (топ-10):");
            r.lines().skip(1).map(l -> l.split(",")).map(a -> new Score(a[1], Integer.parseInt(a[2])))
                    .sorted(Comparator.comparingInt(Score::score).reversed()).limit(10)
                    .forEach(s -> System.out.println(s.player() + " — " + s.score()));
        } catch (IOException e) {
            System.err.println("Ошибка чтения результатов: " + e.getMessage());
        }
    }

    private static void writeScore(String player, int score) {
        try {
            boolean header = !Files.exists(SCORES);
            try (BufferedWriter w = Files.newBufferedWriter(SCORES, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                if (header) {
                    w.write("ts,player,score");
                    w.newLine();
                }
                w.write(LocalDateTime.now() + "," + player + "," + score);
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("Не удалось записать очки: " + e.getMessage());
        }
    }

    private record Score(String player, int score) {}
}