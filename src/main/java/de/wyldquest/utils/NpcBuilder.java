package de.wyldquest.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.wyldquest.Dungeon;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class NpcBuilder {

    private static final Map<Player, ArrayList<EntityPlayer>> npcs = new HashMap<>();

    public void createNPC(String name, Player player, String playerskinname) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld)Bukkit.getWorlds().get(0)).getHandle();
        UUID uuid = UUID.randomUUID();
        GameProfile gameProfile = new GameProfile(uuid, name);

        SkinFetcher fetcher = new SkinFetcher();
        String[] skin =  fetcher.getSkin(fetcher.getUUIDasString(playerskinname));
        gameProfile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));
        EntityPlayer entityPlayer = new EntityPlayer(server, world, gameProfile);
        DataWatcher dataWatcher = entityPlayer.ai();
        byte b = 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40;
        dataWatcher.b(new DataWatcherObject<>(17, DataWatcherRegistry.a), b);
        entityPlayer.a(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
        Location location = player.getLocation();

        PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, entityPlayer);
        PacketPlayOutNamedEntitySpawn namedEntitySpawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entityPlayer.ae(), entityPlayer.ai(), true);
        PacketPlayOutEntityHeadRotation entityHeadRotation = new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) (player.getLocation().getYaw() * 256 / 360));
        PacketPlayOutPlayerInfo remove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, entityPlayer);

        PlayerConnection connection = ((CraftPlayer) player).getHandle().b;

        connection.a(playerInfo);
        connection.a(namedEntitySpawn);
        connection.a(entityMetadata);
        connection.a(entityHeadRotation);

        new BukkitRunnable() {

            @Override
            public void run() {
                connection.a(remove);
            }
        }.runTaskLater(Dungeon.getPlugin(Dungeon.class),100);

        File file = new File("plugins/Dungeons/","npcs.yml");
        FileConfiguration yml = YamlConfiguration.loadConfiguration(file);
        int id;
        if(yml.get("last-created-npc-id") == null)  {
            yml.set("last-created-npc-id", 0);
            id = 0;
        } else {
            id = yml.getInt("last-created-npc-id")+1;
            yml.set("last-created-npc-id", id);
        }
        System.out.println("npc."+id);
        yml.set("npc."+id+".name", name);
        yml.set("npc."+id+".uuid", uuid.toString());
        yml.set("npc."+id+".skinsuuid", skin[2]);
        yml.set("npc."+id+".value", skin[0]);
        yml.set("npc."+id+".signature", skin[1]);
        yml.set("npc."+id+".location", location);
        if(!npcs.containsKey(player)) {
            npcs.put(player, new ArrayList<>());
            npcs.get(player).add(entityPlayer);
        } else {
            npcs.get(player).add(entityPlayer);
        }

        try {
            yml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNPC(int id, Player player) {
        Npc npc = new Npc(id);
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld)Bukkit.getWorlds().get(0)).getHandle();
        GameProfile gameProfile = new GameProfile(npc.getUUID(), npc.getName());
        gameProfile.getProperties().put("textures", new Property("textures", npc.getSkinValue(), npc.getSkinSignature()));

        EntityPlayer entityPlayer = new EntityPlayer(server, world, gameProfile);
        DataWatcher dataWatcher = entityPlayer.ai();
        byte b = 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40;
        dataWatcher.b(new DataWatcherObject<>(17, DataWatcherRegistry.a), b);
        Location location = npc.getLocation();
        entityPlayer.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        PacketPlayOutPlayerInfo playerInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, entityPlayer);
        PacketPlayOutNamedEntitySpawn namedEntitySpawn = new PacketPlayOutNamedEntitySpawn(entityPlayer);
        PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(entityPlayer.ae(), entityPlayer.ai(), true);
        PacketPlayOutEntityHeadRotation entityHeadRotation = new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) (location.getYaw() * 256 / 360));
        PacketPlayOutPlayerInfo remove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e, entityPlayer);

        PlayerConnection connection = ((CraftPlayer) player).getHandle().b;

        connection.a(playerInfo);
        connection.a(namedEntitySpawn);
        connection.a(entityMetadata);
        connection.a(entityHeadRotation);

        entityPlayer.cm();

        new BukkitRunnable() {

            @Override
            public void run() {
                connection.a(remove);
            }
        }.runTaskLater(Dungeon.getPlugin(Dungeon.class),100);
        if(!npcs.containsKey(player)) {
            npcs.put(player, new ArrayList<>());
            npcs.get(player).add(entityPlayer);
        } else {
            npcs.get(player).add(entityPlayer);
        }
        new BukkitRunnable() {

            @Override
            public void run() {
                double distance = location.distance(player.getLocation());
                if(distance <= 10) {
                    location.setDirection(player.getLocation().subtract(location).toVector());

                    PacketPlayOutEntityHeadRotation entityHeadRotation = new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) (location.getYaw() * 256 / 360));
                    PacketPlayOutEntity.PacketPlayOutEntityLook entityLook = new PacketPlayOutEntity.PacketPlayOutEntityLook(entityPlayer.ae(), (byte) (location.getYaw() * 256 / 360), (byte) (location.getPitch() * 256 / 360), true);
                    connection.a(entityHeadRotation);
                    connection.a(entityLook);
                }
            }
        }.runTaskTimer(Dungeon.getPlugin(Dungeon.class), 1, 1);

    }

    public List<Integer> getNPCs() {
        File file = new File("plugins/Dungeons/","npcs.yml");
        FileConfiguration yml = YamlConfiguration.loadConfiguration(file);
        List<String> stringList = yml.getConfigurationSection("npc").getKeys(false).stream().toList();
        return stringList.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
    }

    public static Map<Player, ArrayList<EntityPlayer>> getNpcs() {
        return npcs;
    }
}
