package dev._2lstudios.advancedauth.hooks;

import org.bukkit.entity.Player;

public interface ProxyHook {
    public void sendServer (final Player player, final String server) throws Exception;
}
