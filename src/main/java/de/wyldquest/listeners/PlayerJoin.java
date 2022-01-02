package de.wyldquest.listeners;

import de.wyldquest.utils.NpcBuilder;
import de.wyldquest.utils.PacketReader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(ChatColor.YELLOW+player.getName() + " joined the game ("+ Bukkit.getOnlinePlayers().size() +"/6)");
        PacketReader reader = new PacketReader(player);
        reader.inject();
        NpcBuilder npcBuilder = new NpcBuilder();
        if(!npcBuilder.getNPCs().isEmpty()) {
            for (int i : npcBuilder.getNPCs()) {
                npcBuilder.setNPC(i, player);
            }
        }
    }
}
