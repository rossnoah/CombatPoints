package dev.noah.combatpoints;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatPoints extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.saveDefaultConfig();

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new CombatPointsPlaceholder(this).register(); //
        }else{
            getLogger().warning("PlaceholderAPI not found, placeholders will not work.");
        }

        Bukkit.getPluginManager().registerEvents(new DeathListener(this),this);
        getCommand("combatpoints").setExecutor(new CombatPointsCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
