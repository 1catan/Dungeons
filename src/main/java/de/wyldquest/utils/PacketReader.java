package de.wyldquest.utils;

import com.mojang.authlib.GameProfile;
import de.wyldquest.Dungeon;
import de.wyldquest.listeners.NpcClickEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class PacketReader {

    private Player player;
    private Channel channel;
    public static Map<UUID, Channel> channels = new HashMap<>();

    public PacketReader(Player player) {
        this.player = player;
        CraftPlayer cp = (CraftPlayer) player;
        channel = cp.getHandle().b.a.k;
    }

    public void inject() {
        if(channel.pipeline().get("PacketInjector") != null) {
            return;
        }
        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {
            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, Packet<?> packet, List<Object> list) throws Exception {
                list.add(packet);
                readPacket(player, packet);
                channels.put(player.getUniqueId(), channel);
            }
        });
    }
    public void uninject() {
        if(channel.pipeline().get("PacketInjector") != null) {
            channel.pipeline().remove("PacketInjector");
            channels.remove(player.getUniqueId());
        }
    }

    public void readPacket(Player player, Packet<?> packet) {
        if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            PacketPlayInUseEntity packet1 = (PacketPlayInUseEntity) packet;

            int id = (int) getValue(packet1, "a");

            Object instance = getValue(packet1, "b");
            Class<?>[] classes = PacketPlayInUseEntity.class.getDeclaredClasses();
            Class<?> Dclazz = null;
            for(Class<?> c : classes) {
                if(c.getName().endsWith("d")) {
                    Dclazz = c;
                }
            }
            if(instance.getClass() == Dclazz) {
                String hand = getValue(instance, "a").toString();
                if(hand.equals("OFF_HAND")) {
                    return;
                }
            }
            String action = getValueFromMethod(getValue(packet1, "b"), "a").toString();
            if(action.equals("ATTACK")) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Dungeon.getPlugin(Dungeon.class), () -> {
                    ArrayList<EntityPlayer> list = NpcBuilder.getNpcs().get(player);
                    for(EntityPlayer entityPlayer : list) {
                        if(entityPlayer.ae() == id) {
                            System.out.println("Left Click NPC");
                            player.sendMessage("Left Click NPC");
                            Bukkit.getServer().getPluginManager().callEvent(new NpcClickEvent(player, entityPlayer, NpcClickType.LEFT));
                        }

                    }
                });
            }
            if(action.equals("INTERACT_AT")) {
                return;
            }
            if(action.equals("INTERACT")) {

                Bukkit.getScheduler().scheduleSyncDelayedTask(Dungeon.getPlugin(Dungeon.class), () -> {
                    ArrayList<EntityPlayer> list = NpcBuilder.getNpcs().get(player);
                    for(EntityPlayer entityPlayer : list) {
                        if(entityPlayer.ae() == id) {
                            System.out.println("Right Click NPC");
                            player.sendMessage("Right Click NPC");
                            Bukkit.getServer().getPluginManager().callEvent(new NpcClickEvent(player, entityPlayer, NpcClickType.RIGHT));
                        }

                    }




                });
            }
        }
    }

    private Object getValueFromMethod(Object instance, String methodName) {
        Object r = null;
        try {
            Method method = instance.getClass().getDeclaredMethod(methodName);
            method.setAccessible(true);
            r = method.invoke(instance);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return r;
    }
    private Object getValue(Object instance, String name) {
        Object result = null;
        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            result = field.get(instance);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
