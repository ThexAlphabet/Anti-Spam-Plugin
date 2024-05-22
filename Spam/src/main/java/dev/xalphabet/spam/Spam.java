package dev.xalphabet.spam;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Spam extends JavaPlugin implements Listener {
    private Map<UUID, Long> cooldowns = new HashMap<>();
    private Map<UUID, String> lastMessage = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerChat(PlayerChatEvent e) {
        if (e.getPlayer().hasPermission("spam.bypass")) { // Note to self this is if player has bypass permission!
            return;
        }

        UUID playerId = e.getPlayer().getUniqueId();

        String currentMessage = e.getMessage();
        if (lastMessage.containsKey(playerId) && lastMessage.get(playerId).equals(currentMessage)) {
            e.getPlayer().sendMessage(ChatColor.RED + "Please do not repeat the same message!");
            e.setCancelled(true);
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (cooldowns.containsKey(playerId) && (currentTime - cooldowns.get(playerId)) < 3000) {
            e.getPlayer().sendMessage(ChatColor.RED + "Please wait 3 seconds before sending another message!");
            e.setCancelled(true);
            return;
        }

        cooldowns.put(playerId, currentTime);
        lastMessage.put(playerId, currentMessage);
    }
}
