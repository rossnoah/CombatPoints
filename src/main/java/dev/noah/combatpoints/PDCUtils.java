package dev.noah.combatpoints;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class PDCUtils {

    public static final String PLUGIN_KEY = "combatpoints";
    public static final String POINTS_KEY = "points";
    public static NamespacedKey POINTS_NSK = new NamespacedKey(PLUGIN_KEY,POINTS_KEY);

    public static int getPoints(Player player){
        Integer value = player.getPersistentDataContainer().get(POINTS_NSK, PersistentDataType.INTEGER);
        if(value == null){
            return 0;
        }
        return value;
    }

    public static void setPoints(Player player, int points){
        player.getPersistentDataContainer().set(POINTS_NSK, PersistentDataType.INTEGER, points);
    }

}
