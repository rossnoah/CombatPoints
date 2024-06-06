package dev.noah.combatpoints;

import dev.noah.combatpoints.listeners.DeathListener;
import dev.noah.combatpoints.listeners.SaveLoadListener;
import dev.noah.combatpoints.util.DataUtils;
import dev.noah.combatpoints.util.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CombatPoints extends JavaPlugin {

    private SQLite sqlite;

    @Override
    public void onEnable() {
        // Plugin startup logic



        this.saveDefaultConfig();

        sqlite = new SQLite(this);
        sqlite.createTable();

        //load data for online players
        Bukkit.getOnlinePlayers().forEach(player -> {
            DataUtils.setPoints(player,sqlite.getPoints(player.getUniqueId().toString()));
        });


        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            new CombatPointsPlaceholder(this).register(); //
        }else{
            getLogger().warning("PlaceholderAPI not found, placeholders will not work.");
        }

        Bukkit.getPluginManager().registerEvents(new DeathListener(this),this);
        Bukkit.getPluginManager().registerEvents(new SaveLoadListener(this),this);
        getCommand("combatpoints").setExecutor(new CombatPointsCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(String key : DataUtils.getPointsMap().keySet()){
            sqlite.setPoints(key,DataUtils.getPointsMap().get(key));
        }
        sqlite.close();
    }

    public SQLite getSQLite(){
        return sqlite;
    }
}
