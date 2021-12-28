package de.wyldquest.listeners;

import de.wyldquest.utils.PacketReader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PacketReader reader = new PacketReader(player);
        reader.uninject();
    }
}
