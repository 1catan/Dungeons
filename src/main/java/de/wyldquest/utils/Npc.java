package de.wyldquest.utils;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class Npc {

    private int id;

    public Npc(int id) {
        this.id = id;
    }

    public int getEntityId() {
        return (int) getData("entity-id");
    }

    public String getName() {
        return (String) getData("name");
    }

    public UUID getUUID() {
        String uuid = (String) getData("uuid");
        return UUID.fromString(uuid);
    }

    public String getSkinUUID() {
        return (String) getData("skinsuuid");
    }

    public String getSkinValue() {
        return (String) getData("value");
    }

    public String getSkinSignature() {
        return (String) getData("signature");
    }

    public Location getLocation() {
        return (Location) getData("location");
    }

    private Object getData(String data) {
        File file = new File("plugins/Dungeons/","npcs.yml");
        FileConfiguration yml = YamlConfiguration.loadConfiguration(file);
        return yml.get("npc."+id+"."+data);
    }
}
