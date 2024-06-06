package dev.noah.combatpoints.util;

import org.bukkit.entity.Player;

import java.util.HashMap;

public class DataUtils {

    private static HashMap<String,Integer> pointsMap = new HashMap<>();

    public static int getPoints(Player player){
        Integer value = pointsMap.get(player.getUniqueId().toString());
        if(value == null){
            return 0;
        }
        return value;
    }

    public static void setPoints(Player player, int points){
        pointsMap.put(player.getUniqueId().toString(),points);
    }

    public static HashMap<String,Integer> getPointsMap(){
        return pointsMap;
    }

}
