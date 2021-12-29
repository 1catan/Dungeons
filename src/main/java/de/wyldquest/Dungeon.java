package de.wyldquest;

import de.wyldquest.commands.DungeonCommand;
import de.wyldquest.listeners.PlayerJoin;
import de.wyldquest.listeners.PlayerQuit;
import de.wyldquest.utils.PacketReader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Dungeon extends JavaPlugin {

    private Dungeon instance;

    @Override
    public void onEnable() {
        instance = this;
        this.createFile();
        this.getCommand("dungeon").setExecutor(new DungeonCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        if(!Bukkit.getOnlinePlayers().isEmpty()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PacketReader packetReader = new PacketReader(player);
                packetReader.inject();
            }
        }
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PacketReader packetReader = new PacketReader(player);
            packetReader.uninject();
        }
    }
    public void createFile() {
        File file = new File("plugins/Dungeons/","npcs.yml");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Dungeon getInstance() {
        return this.instance;
    }
}
