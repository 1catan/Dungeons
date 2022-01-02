package de.wyldquest.listeners;

import de.wyldquest.utils.NpcBuilder;
import de.wyldquest.utils.PacketReader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PacketReader reader = new PacketReader(player);
        reader.uninject();
        NpcBuilder.getNpcs().remove(player);
        System.out.println(Bukkit.getScheduler().getPendingTasks().size());
    }
}
