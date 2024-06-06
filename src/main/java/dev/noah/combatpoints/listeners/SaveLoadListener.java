package dev.noah.combatpoints.listeners;

import dev.noah.combatpoints.CombatPoints;
import dev.noah.combatpoints.util.DataUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SaveLoadListener implements Listener {

    private final CombatPoints plugin;
    public SaveLoadListener(CombatPoints plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(plugin,()->{
            DataUtils.setPoints(event.getPlayer(),plugin.getSQLite().getPoints(event.getPlayer().getUniqueId().toString()));
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(plugin,()->{
            plugin.getSQLite().setPoints(event.getPlayer().getUniqueId().toString(),DataUtils.getPoints(event.getPlayer()));
            DataUtils.getPointsMap().remove(event.getPlayer().getUniqueId().toString());
        });

    }

}
