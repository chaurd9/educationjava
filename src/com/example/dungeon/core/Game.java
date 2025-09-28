package com.example.dungeon.core;

import com.example.dungeon.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final GameState state = new GameState();
    private final Map<String, Command> commands = new LinkedHashMap<>();

    // Демонстрация различия ошибок компиляции и выполнения:

    // Ошибка компиляции
    //    String s = 0;

    // Ошибка выполнения
    //    int x = 999666333 / 0;

    static {
        WorldInfo.touch("Game");
    }

    public Game() {
        registerCommands();
        bootstrapWorld();
    }

    private void registerCommands() {
        commands.put("help", (ctx, args, in) -> {
            System.out.println("Команды: " + String.join(", ", commands.keySet()));
            System.out.println();
        });

        commands.put("gc-stats", (ctx, args, in) -> {
            Runtime rt = Runtime.getRuntime();
            long free = rt.freeMemory(), total = rt.totalMemory(), used = total - free;
            System.out.println("Память: used=" + used + " free=" + free + " total=" + total);
            System.out.println();
        });

        commands.put("alloc", (ctx, args, in) -> {
            Runtime rt = Runtime.getRuntime();
            long before = rt.freeMemory();
            byte[] temp = new byte[1_000_000];
            System.out.println("Выделено 1 МБ. Свободно до: " + before);
            temp = null;
            System.gc();
            System.out.println("После GC: свободно " + rt.freeMemory());
            System.out.println();
        });

        commands.put("about", (ctx, args, in) -> {
            System.out.println("DungeonMini, Автор Влад, версия 1.0");
            System.out.println();
        });

        commands.put("look", (ctx, args, in) -> {
            System.out.println(ctx.getCurrent().describe());
            System.out.println();
        });

        commands.put("status", (ctx, args, in) -> {
            Player player = ctx.getPlayer();
            System.out.println("=== СТАТУС ИГРОКА ===");
            System.out.println("Имя: " + player.getName());
            System.out.println("Здоровье: " + player.getHp() + "/" + player.getMaxHp() + " HP");
            System.out.println("Атака: " + player.getAttack());
            System.out.println("Счет: " + ctx.getScore());
            System.out.println("Размер инвентаря: " + player.getInventory().size() + "/5");
            System.out.println();
        });

        commands.put("move", (ctx, args, in) -> {
            if (args.isEmpty()) throw new InvalidCommandException("Укажите направление");
            String direction = args.get(0).toLowerCase();
            Room current = ctx.getCurrent();

            if (current.isExitLocked(direction)) {
                String keyName = current.getExitKeyName(direction);
                throw new InvalidCommandException("Выход " + direction + " заперт. Нужен ключ: " + keyName);
            }

            Room neighbor = current.getNeighbors().get(direction);
            if (neighbor == null) throw new InvalidCommandException("Нет пути на " + direction);

            ctx.setCurrent(neighbor);
            System.out.println("Вы перешли в: " + ctx.getCurrent().getName());
            System.out.println(ctx.getCurrent().describe());
            System.out.println();
        });

        commands.put("take", (ctx, args, in) -> {
            if (args.isEmpty()) throw new InvalidCommandException("Укажите имя предмета");
            String itemName = String.join(" ", args);
            Room current = ctx.getCurrent();

            if (ctx.getPlayer().getInventory().size() >= 5) {
                throw new InvalidCommandException("Инвентарь полон! Максимум 5 предметов. Используйте лобби для хранения.");
            }

            Item item = current.getItems().stream()
                    .filter(i -> i.getName().equalsIgnoreCase(itemName))
                    .findFirst()
                    .orElseThrow(() -> new InvalidCommandException("Предмет не найден: " + itemName));
            current.getItems().remove(item);
            ctx.getPlayer().getInventory().add(item);
            System.out.println("Взято: " + item.getName());

            if (current.getName().equals("Пещера")) {
                boolean hasLegendarySword = ctx.getPlayer().getInventory().stream()
                        .anyMatch(i -> i.getName().equalsIgnoreCase("Легендарный меч"));
                boolean hasLargePotion = ctx.getPlayer().getInventory().stream()
                        .anyMatch(i -> i.getName().equalsIgnoreCase("Большое зелье"));
                if (hasLegendarySword && hasLargePotion) {
                    Monster wolf2 = new Monster("Волк-воин", 2, 15);
                    ctx.getCurrent().setMonster(wolf2);
                    System.out.println("Из глубины пещеры появляется " + wolf2.getName() + " (ур. " + wolf2.getLevel() + ")!");
                    System.out.println("Он охраняет сокровища и не позволит вам уйти!");
                    System.out.println();
                }
            }
            System.out.println();
        });

        commands.put("inventory", (ctx, args, in) -> {
            List<Item> inventory = ctx.getPlayer().getInventory();
            if (inventory.isEmpty()) {
                System.out.println("Инвентарь пуст");
                System.out.println();
                return;
            }
            Map<String, List<Item>> grouped = inventory.stream()
                    .collect(Collectors.groupingBy(i -> i.getClass().getSimpleName()));
            grouped.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(e -> {
                        String type = e.getKey();
                        List<String> names = e.getValue().stream()
                                .map(Item::getName)
                                .sorted()
                                .toList();
                        System.out.println("- " + type + " (" + names.size() + "): " + String.join(", ", names));
                    });
            System.out.println("Слотов занято: " + inventory.size() + "/5");
            System.out.println();
        });

        commands.put("use", (ctx, args, in) -> {
            if (args.isEmpty()) throw new InvalidCommandException("Укажите имя предмета");
            String itemName = String.join(" ", args);
            Item item = ctx.getPlayer().getInventory().stream()
                    .filter(i -> i.getName().equalsIgnoreCase(itemName))
                    .findFirst()
                    .orElseThrow(() -> new InvalidCommandException("Предмет не найден: " + itemName));

            item.apply(ctx);
            System.out.println();
        });

        commands.put("fight", (ctx, args, in) -> {
            Monster monster = ctx.getCurrent().getMonster();
            if (monster == null) throw new InvalidCommandException("В комнате нет монстра");
            Player player = ctx.getPlayer();
            int playerDmg = player.getAttack();
            monster.setHp(monster.getHp() - playerDmg);
            System.out.println("Вы бьёте " + monster.getName() + " на " + playerDmg + ". HP монстра: " + monster.getHp());
            if (monster.getHp() <= 0) {
                System.out.println(monster.getName() + " побеждён!");
                ctx.getCurrent().setMonster(null);

                if (monster.getName().equals("Волк")) {
                    ctx.addScore(15);
                    ctx.getCurrent().getItems().add(new Key("Ключ от пещеры"));
                    System.out.println("Волк уронил: Ключ от пещеры");
                    System.out.println("Получено +15 очков!");
                } else if (monster.getName().equals("Волк-воин")) {
                    ctx.addScore(30);
                    ctx.getCurrent().getItems().add(new Gem("Драгоценный камень"));
                    System.out.println("Волк-воин уронил: Драгоценный камень");
                    System.out.println("Получено +30 очков!");
                    System.out.println("\n🎉 ВАШЕ ПРИКЛЮЧЕНИЕ ЗАВЕРШЕНО! 🎉");
                    System.out.println("Вы победили могучего Волка-воина и нашли все сокровища!");
                    System.out.println("Для выхода из игры введите команду: exit");
                }
                System.out.println();
                return;
            }

            int monsterDmg = monster.getLevel() + 1;
            player.setHp(player.getHp() - monsterDmg);
            System.out.println("Монстр отвечает на " + monsterDmg + ". Ваше HP: " + player.getHp());
            if (player.getHp() <= 0) {
                System.out.println("Вы погибли!");
                System.out.println();
                System.exit(0);
            } else {
                System.out.println("Введите fight снова.");
                System.out.println();
            }
        });

        commands.put("unlock", (ctx, args, in) -> {
            if (args.isEmpty()) throw new InvalidCommandException("Укажите направление");
            String direction = args.get(0).toLowerCase();
            Room current = ctx.getCurrent();
            if (!current.isExitLocked(direction)) {
                throw new InvalidCommandException("Выход " + direction + " не заперт");
            }
            String keyName = current.getExitKeyName(direction);
            Item key = ctx.getPlayer().getInventory().stream()
                    .filter(i -> i.getName().equalsIgnoreCase(keyName))
                    .findFirst()
                    .orElseThrow(() -> new InvalidCommandException("Нужен ключ: " + keyName));
            current.unlockExit(direction);
            ctx.getPlayer().getInventory().remove(key);
            System.out.println("Выход " + direction + " разблокирован с помощью " + keyName);
            System.out.println();
        });

        commands.put("rest", (ctx, args, in) -> {
            if (!ctx.getCurrent().getName().equals("Лобби")) {
                throw new InvalidCommandException("Отдых возможен только в лобби");
            }
            executeRest(ctx);
        });

        commands.put("store", (ctx, args, in) -> {
            if (!ctx.getCurrent().getName().equals("Лобби")) {
                throw new InvalidCommandException("Сундук доступен только в лобби");
            }
            if (args.isEmpty()) throw new InvalidCommandException("Укажите имя предмета");
            String itemName = String.join(" ", args);
            Player player = ctx.getPlayer();
            Item item = player.getInventory().stream()
                    .filter(i -> i.getName().equalsIgnoreCase(itemName))
                    .findFirst()
                    .orElseThrow(() -> new InvalidCommandException("Предмет не найден в инвентаре: " + itemName));
            List<Item> chest = ctx.getCurrent().getChest();
            if (chest.size() >= 4) throw new InvalidCommandException("Сундук полон (максимум 4 слота)");
            player.getInventory().remove(item);
            chest.add(item);
            System.out.println("Предмет " + item.getName() + " положен в сундук");
            System.out.println();
        });

        commands.put("retrieve", (ctx, args, in) -> {
            if (!ctx.getCurrent().getName().equals("Лобби")) {
                throw new InvalidCommandException("Сундук доступен только в лобби");
            }
            if (args.isEmpty()) throw new InvalidCommandException("Укажите имя предмета");
            String itemName = String.join(" ", args);
            List<Item> chest = ctx.getCurrent().getChest();
            Item item = chest.stream()
                    .filter(i -> i.getName().equalsIgnoreCase(itemName))
                    .findFirst()
                    .orElseThrow(() -> new InvalidCommandException("Предмет не найден в сундуке: " + itemName));
            if (ctx.getPlayer().getInventory().size() >= 5) {
                throw new InvalidCommandException("Инвентарь полон! Максимум 5 предметов. Освободите место.");
            }
            chest.remove(item);
            ctx.getPlayer().getInventory().add(item);
            System.out.println("Предмет " + item.getName() + " взят из сундука");
            System.out.println();
        });

        commands.put("save", (ctx, args, in) -> {
            SaveLoad.save(ctx);
            System.out.println();
        });

        commands.put("load", (ctx, args, in) -> {
            SaveLoad.load(ctx);
            System.out.println();
        });

        commands.put("scores", (ctx, args, in) -> {
            SaveLoad.printScores();
            System.out.println();
        });

        commands.put("exit", (ctx, args, in) -> {
            System.out.println("Спасибо за игру! До свидания!");
            System.out.println();
            System.exit(0);
        });
    }

    private void executeRest(GameState ctx) {
        Player player = ctx.getPlayer();
        if (player.getHp() >= player.getMaxHp()) {
            System.out.println("Вы полностью здоровы!");
            return;
        }

        int cost = 10;
        int availablePoints = Math.min(cost, ctx.getScore());
        ctx.addScore(-availablePoints);

        int healed = 0;
        while (player.getHp() < player.getMaxHp()) {
            player.setHp(player.getHp() + 1);
            healed++;
        }

        System.out.println("Вы отдохнули. Здоровье восстановлено +" + healed + " [" + player.getHp() + "/" + player.getMaxHp() + " HP]");
        if (availablePoints > 0) {
            System.out.println("Списано " + availablePoints + " очков. Текущий счет: " + ctx.getScore());
        } else {
            System.out.println("Очки не списаны (недостаточно очков). Текущий счет: " + ctx.getScore());
        }
    }

    private void bootstrapWorld() {
        Player hero = new Player("Герой", 12, 15, 4);
        state.setPlayer(hero);

        Room square = new Room("Площадь", "Каменная площадь с фонтаном. Отсюда можно отправиться в приключение или отдохнуть в лобби.");
        Room forest = new Room("Лес", "Шелест листвы и птичий щебет.");
        Room cave = new Room("Пещера", "Темно и сыро.");
        Room lobby = new Room("Лобби", "Уютное лобби для отдыха и хранения вещей.");

        square.getNeighbors().put("north", forest);
        square.getNeighbors().put("lobby", lobby);

        forest.getNeighbors().put("south", square);
        forest.getNeighbors().put("east", cave);
        forest.lockExit("east", "Ключ от пещеры");

        cave.getNeighbors().put("west", forest);
        lobby.getNeighbors().put("back", square);

        forest.getItems().add(new Potion("Малое зелье", 5));
        forest.setMonster(new Monster("Волк", 1, 10));
        cave.getItems().add(new Weapon("Легендарный меч", 10));
        cave.getItems().add(new Potion("Большое зелье", 15));

        state.getRooms().put(square.getName(), square);
        state.getRooms().put(forest.getName(), forest);
        state.getRooms().put(cave.getName(), cave);
        state.getRooms().put(lobby.getName(), lobby);

        state.setCurrent(square);
    }

    public void run() {
        System.out.println("DungeonMini - Ваше приключение начинается!");
        System.out.println("'help' - список команд");
        System.out.println();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("> ");
                String line = in.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.isEmpty()) continue;
                List<String> parts = Arrays.asList(line.split("\s+"));
                String cmd = parts.getFirst().toLowerCase(Locale.ROOT);
                List<String> args = parts.subList(1, parts.size());
                Command c = commands.get(cmd);
                try {
                    if (c == null) throw new InvalidCommandException("Неизвестная команда: " + cmd);
                    c.execute(state, args, in);
                } catch (InvalidCommandException e) {
                    System.out.println("Ошибка: " + e.getMessage());
                    System.out.println();
                } catch (Exception e) {
                    System.out.println("Непредвиденная ошибка: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                    System.out.println();
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода: " + e.getMessage());
        }
    }
}