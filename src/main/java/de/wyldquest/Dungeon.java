package de.wyldquest;

import de.wyldquest.commands.DungeonCommand;
import de.wyldquest.listeners.PlayerJoin;
import de.wyldquest.utils.PacketReader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Dungeon extends JavaPlugin {

    private Dungeon instance;

    @Override
    public void onEnable() {
        instance = this;
        this.createFolder();
        this.getCommand("dungeon").setExecutor(new DungeonCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
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
    public void createFolder() {
        File folder = new File("plugins/Dungeons/npcs");
        if(!folder.exists()) {
            folder.mkdir();
        }
    }

    public Dungeon getInstance() {
        return this.instance;
    }
}
