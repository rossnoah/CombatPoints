package dev.noah.combatpoints;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatPoints extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new CombatPointsPlaceholder(this).register(); //
        }else{
            getLogger().warning("PlaceholderAPI not found, placeholders will not work.");
        }

        Bukkit.getPluginManager().registerEvents(new DeathListener(),this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
