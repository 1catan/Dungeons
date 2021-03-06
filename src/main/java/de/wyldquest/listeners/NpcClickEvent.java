package de.wyldquest.listeners;

import de.wyldquest.utils.NpcClickType;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NpcClickEvent extends Event implements Cancellable {

    private Player player;
    private EntityPlayer entityPlayer;
    private NpcClickType clickType;
    private boolean isCancelled;
    private static final HandlerList handlers = new HandlerList();

    public NpcClickEvent(Player player, EntityPlayer entityPlayer, NpcClickType clickType) {
        this.player = player;
        this.entityPlayer = entityPlayer;
        this.clickType = clickType;
        this.isCancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    public NpcClickType getClickType() {
        return clickType;
    }
}
