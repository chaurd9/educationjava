package com.example.dungeon.core;

import com.example.dungeon.model.GameState;
import java.util.List;
import java.io.BufferedReader;

@FunctionalInterface
public interface Command {
    void execute(GameState ctx, List<String> args, BufferedReader in);
}
