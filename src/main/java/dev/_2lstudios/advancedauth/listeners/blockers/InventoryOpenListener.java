package dev._2lstudios.advancedauth.listeners.blockers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;

import dev._2lstudios.advancedauth.AdvancedAuth;

public class InventoryOpenListener extends BlockerListener {
    public InventoryOpenListener(AdvancedAuth plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getPlayer() instanceof Player) {
            if (!this.isAllowed((Player) e.getPlayer(), "deny-inventory")) {
                e.setCancelled(true);
            }
        }
    }
}
