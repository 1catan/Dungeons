package de.wyldquest;

import de.wyldquest.commands.DungeonCommand;
import de.wyldquest.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Dungeon extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("dungeon").setExecutor(new DungeonCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
