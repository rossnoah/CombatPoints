package dev.noah.combatpoints;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatPoints extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.saveDefaultConfig();
        boolean isValidConfig = verifyConfig();

        if(!isValidConfig){
            getLogger().warning("Invalid config, disabling plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new CombatPointsPlaceholder(this).register(); //
        }else{
            getLogger().warning("PlaceholderAPI not found, placeholders will not work.");
        }

        Bukkit.getPluginManager().registerEvents(new DeathListener(),this);
        getCommand("combatpoints").setExecutor(new CombatPointsCommand(this));

    }

    private boolean verifyConfig(){
        boolean valid = true;
        if(!this.getConfig().contains("messages.no-permission")){
            getLogger().warning("Missing no-permission message in config.");
            valid = false;
        }
        if(!this.getConfig().contains("messages.no-console")){
            getLogger().warning("Missing no-console message in config.");
            valid = false;
        }
        if(!this.getConfig().contains("messages.points.self")){
            getLogger().warning("Missing points.self message in config.");
            valid = false;
        }
        if(!this.getConfig().contains("messages.points.other")){
            getLogger().warning("Missing points.other message in config.");
            valid = false;
        }
        return valid;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
