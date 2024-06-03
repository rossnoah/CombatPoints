package dev.noah.combatpoints;


import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CombatPointsPlaceholder extends PlaceholderExpansion {

    private final CombatPoints plugin; //

    public CombatPointsPlaceholder(CombatPoints plugin) {
        this.plugin = plugin;
    }

    @Override
    @NotNull
    public String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors()); //
    }

    @Override
    @NotNull
    public String getIdentifier() {
        return "combatpoints";
    }

    @Override
    @NotNull
    public String getVersion() {
        return plugin.getDescription().getVersion(); //
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (params.equalsIgnoreCase("points")) {
            if(offlinePlayer.getPlayer()==null){
                return "0";
            }

            Player player = offlinePlayer.getPlayer();
            return String.valueOf(PDCUtils.getPoints(player));

        }


        return null; //
    }
}